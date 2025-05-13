package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.config.HelloAssoProperties;
import com.frequencies.tombola.dto.helloasso.*;
import com.frequencies.tombola.enums.PaymentMethod;
import com.frequencies.tombola.service.HelloAssoService;
import com.frequencies.tombola.service.auth.HelloAssoAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class HelloAssoServiceImpl implements HelloAssoService {
    private final RestTemplate rest;
    private final HelloAssoAuthService auth;
    private final HelloAssoProperties cfg;

    private HttpHeaders authHeaders() {
        String token = auth.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    @Override
    public HelloAssoFormsResponse getAvailableForms() {
        String url = UriComponentsBuilder
                .fromUriString(cfg.getApiUrl())
                .path("/v5/organizations/{org}/forms")
                .buildAndExpand(cfg.getOrganizationSlug())
                .toUriString();

        ResponseEntity<HelloAssoFormsResponse> resp = rest.exchange(
                url, HttpMethod.GET, new HttpEntity<>(authHeaders()),
                HelloAssoFormsResponse.class
        );
        return resp.getBody();
    }

    @Override
    public List<HelloAssoParticipantDto> getPaidParticipants(String formType, String formSlug) {
        OffsetDateTime from = OffsetDateTime.now().minus(30, ChronoUnit.DAYS);
        OffsetDateTime to   = OffsetDateTime.now().plus(1, ChronoUnit.DAYS);

        List<HelloAssoOrdersResponse.PaymentWrapper> allPayments = new ArrayList<>();
        String continuationToken = null;
        int page = 1, size = 100;

        do {
            HelloAssoOrdersResponse resp = getPayments(
                    formType, formSlug, from, to,
                    null, page, size,
                    continuationToken, null,
                    "Desc", "Date", false
            );
            if (resp == null || resp.getData() == null || resp.getData().isEmpty()) break;
            allPayments.addAll(resp.getData());
            continuationToken = Optional.ofNullable(resp.getPagination())
                    .map(HelloAssoOrdersResponse.Pagination::getContinuationToken)
                    .orElse(null);
            page++;
        } while (continuationToken != null);

        return allPayments.stream()
                .map(this::toDtoFromPayment)
                .filter(p -> p.getEmail() != null && !p.getEmail().isBlank())
                .collect(Collectors.toList());
    }

    @Override
    public List<HelloAssoParticipantDto> getAllParticipants(String formType, String formSlug) {
        List<HelloAssoParticipantDto> paid = getPaidParticipants(formType, formSlug);
        List<HelloAssoParticipantDto> free = getFreeParticipants(formType, formSlug);

        Map<String, HelloAssoParticipantDto> map = new LinkedHashMap<>();
        Stream.concat(
                paid.stream().filter(p -> p.getPaymentMethod() == PaymentMethod.CASH),
                free.stream().filter(p -> p.getPaymentMethod() == PaymentMethod.CASH)
        ).forEach(p -> map.put(p.getEmail().toLowerCase(), p));
        Stream.concat(
                paid.stream().filter(p -> p.getPaymentMethod() != PaymentMethod.CASH),
                free.stream().filter(p -> p.getPaymentMethod() != PaymentMethod.CASH)
        ).forEach(p -> map.putIfAbsent(p.getEmail().toLowerCase(), p));

        return new ArrayList<>(map.values());
    }

    /**
     * Retrieves only participants who have an item with amount == 0.
     * Paginate using pageIndex until a page returns fewer items than pageSize.
     */

    private List<HelloAssoParticipantDto> getFreeParticipants(String formType, String formSlug) {
        HttpHeaders headers = authHeaders();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String from = Instant.now().minus(30, ChronoUnit.DAYS).atZone(ZoneOffset.UTC).format(fmt);
        String to   = Instant.now().plus(1, ChronoUnit.DAYS).atZone(ZoneOffset.UTC).format(fmt);

        Map<String, HelloAssoParticipantDto> emailMap = new HashMap<>();

        //get items (promo code / free)
        List<HelloAssoItemDto> items = new ArrayList<>();
        String cont = null;
        int page = 1, size = 100;
        do {
            String url = UriComponentsBuilder
                    .fromUriString(cfg.getApiUrl())
                    .path("/v5/organizations/{org}/forms/{type}/{slug}/items")
                    .queryParam("from", from)
                    .queryParam("to", to)
                    .queryParam("pageIndex", page++)
                    .queryParam("pageSize", size)
                    .buildAndExpand(cfg.getOrganizationSlug(), formType, formSlug)
                    .toUriString();

            ResponseEntity<HelloAssoItemListResponse> resp = rest.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers),
                    HelloAssoItemListResponse.class
            );
            var data = Optional.ofNullable(resp.getBody())
                    .map(HelloAssoItemListResponse::getData)
                    .orElse(List.of());
            if (data.isEmpty()) break;
            items.addAll(data);
            if (data.size() < size) break;
        } while (true);

        Map<String, List<HelloAssoItemDto>> byEmail = items.stream()
                .filter(item -> item.getPayerEmail() != null && !emailMap.containsKey(item.getPayerEmail().toLowerCase()))
                .collect(Collectors.groupingBy(item -> item.getPayerEmail().toLowerCase()));

        //  building DTO by email
        return byEmail.entrySet().stream()
                .map(entry -> {
                    List<HelloAssoItemDto> group = entry.getValue();
                    HelloAssoItemDto sample = group.get(0);

                    int ticketCount = group.size();
                    String state    = sample.getState();

                    PaymentMethod pm = group.stream().anyMatch(i -> i.getAmount() != null && i.getAmount() > 0)
                            ? PaymentMethod.CARD
                            : PaymentMethod.CASH;

                    return HelloAssoParticipantDto.builder()
                            .firstName    (sample.getPayerFirstName())
                            .lastName     (sample.getPayerLastName())
                            .email        (sample.getPayerEmail())
                            .phone        (null)
                            .ticketNumber (ticketCount)
                            .state        (state)
                            .paymentMethod(pm)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private HelloAssoParticipantDto toDtoFromPayment(HelloAssoOrdersResponse.PaymentWrapper payment) {
        var payer = payment.getPayer();
        int tickets = Optional.ofNullable(payment.getItems()).orElse(List.of()).stream()
                .filter(i -> "Registration".equals(i.getType()))
                .mapToInt(i -> i.getAmount() == 0
                        ? 1
                        : (int)(i.getShareAmount() / i.getShareItemAmount()))
                .sum();

        PaymentMethod pm = PaymentMethod.CARD;
        if ((payment.getPaymentOffLineMean() != null && !payment.getPaymentOffLineMean().isEmpty())
                || "OfflinePayment".equals(payment.getPaymentMeans())) {
            pm = PaymentMethod.CASH;
        }
        String state = pm == PaymentMethod.CASH ? "Paid" : payment.getState();

        return HelloAssoParticipantDto.builder()
                .firstName   (payer.getFirstName())
                .lastName    (payer.getLastName())
                .email       (payer.getEmail())
                .phone       (payer.getPhone())
                .ticketNumber(tickets)
                .state       (state)
                .paymentMethod(pm)
                .build();
    }

    @Override
    public HelloAssoOrdersResponse getPayments(
            String formType, String formSlug,
            OffsetDateTime from, OffsetDateTime to,
            String userSearchKey,
            int pageIndex, int pageSize,
            String continuationToken,
            List<String> states,
            String sortOrder, String sortField, boolean withCount
    ) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String fromStr = from.toInstant().atOffset(ZoneOffset.UTC).format(fmt);
        String toStr   = to  .toInstant().atOffset(ZoneOffset.UTC).format(fmt);

        var builder = UriComponentsBuilder
                .fromUriString(cfg.getApiUrl())
                .path("/v5/organizations/{org}/forms/{type}/{slug}/payments")
                .queryParam("from", fromStr)
                .queryParam("to",   toStr)
                .queryParam("pageSize",   pageSize)
                .queryParam("sortOrder",   sortOrder)
                .queryParam("sortField",   sortField)
                .queryParam("withCount",   withCount);

        if (continuationToken == null) {
            builder.queryParam("pageIndex", pageIndex);
        } else {
            builder.queryParam("continuationToken", continuationToken);
        }
        if (userSearchKey != null) builder.queryParam("userSearchKey", userSearchKey);
        if (states != null && !states.isEmpty())
            builder.queryParam("states", String.join(",", states));

        String url = builder
                .buildAndExpand(cfg.getOrganizationSlug(), formType, formSlug)
                .toUriString();

        ResponseEntity<HelloAssoOrdersResponse> resp = rest.exchange(
                url, HttpMethod.GET, new HttpEntity<>(authHeaders()),
                HelloAssoOrdersResponse.class
        );
        return resp.getBody();
    }
}
