package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.config.HelloAssoProperties;
import com.frequencies.tombola.dto.helloasso.*;
import com.frequencies.tombola.enums.PaymentMethod;
import com.frequencies.tombola.service.HelloAssoService;
import com.frequencies.tombola.service.auth.HelloAssoAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HelloAssoServiceImpl implements HelloAssoService {
    private final RestTemplate rest;
    private final HelloAssoAuthService auth;
    private final HelloAssoProperties cfg;

    private HttpHeaders authHeaders() {
        String token = auth.getAccessToken();
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(token);
        h.setAccept(List.of(MediaType.APPLICATION_JSON));
        return h;
    }

    @Override
    public HelloAssoFormsResponse getAvailableForms() {
        String url = UriComponentsBuilder
                .fromUriString(cfg.getApiUrl())
                .path("/v5/organizations/{org}/forms")
                .buildAndExpand(cfg.getOrganizationSlug())
                .toUriString();

        ResponseEntity<HelloAssoFormsResponse> resp = rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                HelloAssoFormsResponse.class
        );

        return resp.getBody();
    }

    @Override
    public List<HelloAssoParticipantDto> getPaidParticipants(String formType, String formSlug) {
        HttpHeaders headers = authHeaders();

        Instant now = Instant.now();
        String from = now.minus(30, ChronoUnit.DAYS).toString();
        String to   = now.plus(1, ChronoUnit.DAYS).toString();

        // Use the /payments endpoint from the HelloAsso API to get all payments
        String url = UriComponentsBuilder
                .fromUriString(cfg.getApiUrl())
                .path("/v5/organizations/{org}/forms/{type}/{slug}/payments")
                .queryParam("from", from)
                .queryParam("to", to)
                .buildAndExpand(cfg.getOrganizationSlug(), formType, formSlug)
                .toUriString();

        ResponseEntity<HelloAssoOrdersResponse> resp = rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                HelloAssoOrdersResponse.class
        );

        if (resp.getBody() == null || resp.getBody().getData() == null) {
            log.warn("No data received from HelloAsso");
            return List.of();
        }

        log.info("Processing {} payments from HelloAsso", resp.getBody().getData().size());

        return resp.getBody().getData().stream()
                .map(payment -> {
                    var payer = payment.getPayer();

                    // Log payment details for debugging
                    log.info("Processing payment: id={}, payer={}, items={}, paymentMeans={}, paymentOffLineMean={}, state={}, formSlug={}", 
                            payment.getId(), 
                            payer.getEmail(),
                            payment.getItems() != null ? payment.getItems().size() : 0,
                            payment.getPaymentMeans(),
                            payment.getPaymentOffLineMean(),
                            payment.getState(),
                            payment.getOrder() != null ? payment.getOrder().getFormSlug() : "null");

                    // Log first item for debugging
                    if (payment.getItems() != null && !payment.getItems().isEmpty()) {
                        var firstItem = payment.getItems().get(0);
                        log.info("First item: id={}, amount={}, type={}, state={}, name={}", 
                                firstItem.getId(), 
                                firstItem.getAmount(),
                                firstItem.getType(),
                                firstItem.getState(),
                                firstItem.getName());
                    }

                    // Calculate number of tickets using shareAmount/shareItemAmount
                    int ticketCount = 0;
                    if (payment.getItems() != null) {
                        ticketCount = payment.getItems().stream()
                                .filter(item -> "Registration".equals(item.getType()))
                                .mapToInt(item -> {
                                    // For free items (amount = 0), count as 1 ticket
                                    if (item.getAmount() == 0) {
                                        return 1;
                                    }
                                    // For paid items, calculate based on shareAmount/shareItemAmount
                                    double shareItemAmount = item.getShareItemAmount();
                                    if (shareItemAmount > 0) {
                                        return (int) (item.getShareAmount() / shareItemAmount);
                                    }
                                    return 0;
                                })
                                .sum();
                    }

                    // Determine payment method based on paymentMeans, paymentOffLineMean, or free items
                    PaymentMethod paymentMethod = PaymentMethod.CARD; // Default to card payment

                    // Check if there's an offline payment method
                    if (payment.getPaymentOffLineMean() != null && !payment.getPaymentOffLineMean().isEmpty()) {
                        // If there's an offline payment method, it's a cash payment
                        paymentMethod = PaymentMethod.CASH;
                        log.info("Payment method set to CASH due to offline payment mean: {}", payment.getPaymentOffLineMean());
                    } 
                    // Check if paymentMeans indicates a cash payment
                    else if ("OfflinePayment".equals(payment.getPaymentMeans())) {
                        paymentMethod = PaymentMethod.CASH;
                        log.info("Payment method set to CASH due to payment means: {}", payment.getPaymentMeans());
                    }
                    // Check if any item has amount = 0 (promo code/free item)
                    else if (payment.getItems() != null && payment.getItems().stream().anyMatch(item -> item.getAmount() == 0)) {
                        paymentMethod = PaymentMethod.CASH;
                        log.info("Payment method set to CASH due to item having amount = 0");
                    }

                    log.info("Payment method determined: {}", paymentMethod);

                    // For cash payments, always set state to "Paid"
                    String state = PaymentMethod.CASH.equals(paymentMethod) ? "Paid" : payment.getState();

                    return HelloAssoParticipantDto.builder()
                            .firstName(payer.getFirstName())
                            .lastName(payer.getLastName())
                            .email(payer.getEmail())
                            .phone(payer.getPhone())
                            .ticketNumber(ticketCount)
                            .state(state)
                            .paymentMethod(paymentMethod)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HelloAssoParticipantDto> getAllParticipants(String formType, String formSlug) {
        // Get paid participants
        List<HelloAssoParticipantDto> paidParticipants = getPaidParticipants(formType, formSlug);
        log.info("Found {} paid participants", paidParticipants.size());

        // Get free participants
        List<HelloAssoParticipantDto> freeParticipants = getFreeParticipants(formType, formSlug);
        log.info("Found {} free participants", freeParticipants.size());

        // Combine both lists without filtering out duplicates
        List<HelloAssoParticipantDto> allParticipants = new java.util.ArrayList<>();
        allParticipants.addAll(paidParticipants);
        allParticipants.addAll(freeParticipants);

        // Log all participants for debugging
        log.info("All participants: {}", allParticipants);

        // Log count of participants by payment method
        long cardCount = allParticipants.stream().filter(p -> PaymentMethod.CARD.equals(p.getPaymentMethod())).count();
        long cashCount = allParticipants.stream().filter(p -> PaymentMethod.CASH.equals(p.getPaymentMethod())).count();
        log.info("Participants by payment method: CARD={}, CASH={}", cardCount, cashCount);

        return allParticipants;
    }

    /**
     * Helper method to fetch free participants for a given formType+formSlug.
     * This method specifically looks for participants with free tickets or cash payments
     * that might not be properly captured by getPaidParticipants.
     * Uses the /items endpoint which works better for cash payments according to user feedback.
     */
    private List<HelloAssoParticipantDto> getFreeParticipants(String formType, String formSlug) {
        HttpHeaders headers = authHeaders();

        Instant now = Instant.now();
        String from = now.minus(30, ChronoUnit.DAYS).toString();
        String to   = now.plus(1, ChronoUnit.DAYS).toString();

        // Use the /items endpoint from the HelloAsso API to get all items
        String url = UriComponentsBuilder
                .fromUriString(cfg.getApiUrl())
                .path("/v5/organizations/{org}/forms/{type}/{slug}/items")
                .queryParam("from", from)
                .queryParam("to", to)
                .buildAndExpand(cfg.getOrganizationSlug(), formType, formSlug)
                .toUriString();

        try {
            ResponseEntity<HelloAssoItemListResponse> resp = rest.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    HelloAssoItemListResponse.class
            );

            if (resp.getBody() == null || resp.getBody().getData() == null) {
                log.warn("No item data received from HelloAsso");
                return List.of();
            }

            log.info("Processing {} items for free participants", resp.getBody().getData().size());

            // Filter for items with amount = 0 (free items)
            List<HelloAssoParticipantDto> freeParticipants = resp.getBody().getData().stream()
                    .filter(item -> item.getAmount() != null && item.getAmount() == 0)
                    .map(item -> {
                        log.info("Found free item: id={}, payer={}, amount={}, status={}", 
                                item.getId(), 
                                item.getPayerEmail(),
                                item.getAmount(),
                                item.getStatus());

                        return HelloAssoParticipantDto.builder()
                                .firstName(item.getPayerFirstName())
                                .lastName(item.getPayerLastName())
                                .email(item.getPayerEmail())
                                .phone(null) // Phone not available in item data
                                .ticketNumber(1) // Each free item counts as 1 ticket
                                .state("Paid") // Free items are always considered paid
                                .paymentMethod(PaymentMethod.CASH) // Free items are treated as cash payments
                                .build();
                    })
                    .collect(Collectors.toList());

            log.info("Found {} free participants using /items endpoint", freeParticipants.size());
            return freeParticipants;
        } catch (Exception e) {
            log.error("Error fetching free participants: {}", e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public HelloAssoOrdersResponse getPayments(
            String formType,
            String formSlug,
            OffsetDateTime from,
            OffsetDateTime to,
            String userSearchKey,
            int pageIndex,
            int pageSize,
            String continuationToken,
            List<String> states,
            String sortOrder,
            String sortField,
            boolean withCount
    ) {
        HttpHeaders headers = authHeaders();
        var uriBuilder = UriComponentsBuilder
                .fromUriString(cfg.getApiUrl())
                .path("/v5/organizations/{org}/forms/{type}/{slug}/payments")
                .queryParam("from", from.toString())
                .queryParam("to", to.toString())
                .queryParam("pageIndex", pageIndex)
                .queryParam("pageSize", pageSize)
                .queryParam("sortOrder", sortOrder)
                .queryParam("sortField", sortField)
                .queryParam("withCount", withCount);

        if (userSearchKey != null) {
            uriBuilder.queryParam("userSearchKey", userSearchKey);
        }
        if (continuationToken != null) {
            uriBuilder.queryParam("continuationToken", continuationToken);
        }
        if (states != null && !states.isEmpty()) {
            uriBuilder.queryParam("states", String.join(",", states));
        }

        String url = uriBuilder
                .buildAndExpand(cfg.getOrganizationSlug(), formType, formSlug)
                .toUriString();

        ResponseEntity<HelloAssoOrdersResponse> resp = rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                HelloAssoOrdersResponse.class
        );

        if (resp.getBody() != null) {
            log.debug("Nombre de paiements récupérés : {}",
                    resp.getBody().getData() != null ? resp.getBody().getData().size() : 0);
        }

        return resp.getBody();
    }
    }
