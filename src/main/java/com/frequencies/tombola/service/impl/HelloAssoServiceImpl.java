package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.helloasso.HelloAssoFormDto;
import com.frequencies.tombola.dto.helloasso.HelloAssoFormsResponse;
import com.frequencies.tombola.dto.helloasso.HelloAssoOrdersResponse;
import com.frequencies.tombola.dto.helloasso.HelloAssoParticipantDto;
import com.frequencies.tombola.service.HelloAssoService;
import com.frequencies.tombola.service.auth.HelloAssoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HelloAssoServiceImpl implements HelloAssoService {

    private final HelloAssoAuthService authService;
    private final RestTemplate restTemplate;

    @Value("${helloasso.api-url}")
    private String helloAssoBaseUrl;

    @Value("${helloasso.organization-slug}")
    private String orgSlug;

    @Override
    public List<HelloAssoParticipantDto> getPaidParticipants(String formType, String formSlug) {
        // par exemple, sur les 30 derniers jours
        OffsetDateTime to   = OffsetDateTime.now();
        OffsetDateTime from = to.minusDays(30);

        try {
            HelloAssoOrdersResponse resp = getPayments(
                    formType, formSlug,
                    from, to,
                    null,     // pas de searchKey
                    1, 100,   // première page, 100 items max
                    null,     // pas de token
                    List.of("Authorized", "Registered"),  // états à inclure
                    "Desc", "Date",
                    false     // on n’a pas besoin de la pagination complète
            );

            return resp.getData().stream()
                    .filter(w -> "Authorized".equalsIgnoreCase(w.getState())
                            || "Registered".equalsIgnoreCase(w.getState()))
                    .map(w -> {
                        var p = w.getPayer();
                        return HelloAssoParticipantDto.builder()
                                .firstName(p.getFirstName())
                                .lastName( p.getLastName())
                                .email(     p.getEmail())
                                .phone(null)
                                .state(     w.getState())
                                .build();
                    })
                    .toList();

        } catch (org.springframework.web.client.HttpClientErrorException.NotFound ex) {
            // HelloAsso Sandbox ne connaît pas ce form → on renvoie juste une liste vide
            return Collections.emptyList();
        }
    }

    @Override
    public List<HelloAssoFormDto> getAvailableForms() {
        String url = String.format("%s/v5/organizations/%s/forms",
                helloAssoBaseUrl, orgSlug);

        HttpEntity<Void> request = new HttpEntity<>(buildHeaders());
        ResponseEntity<HelloAssoFormsResponse> resp = restTemplate.exchange(
                url, HttpMethod.GET, request, HelloAssoFormsResponse.class
        );

        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            return List.of();
        }
        return resp.getBody().getData();
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
        String url = String.format(
                "%s/v5/organizations/%s/%s/%s/payments" +
                        "?from=%s&to=%s&userSearchKey=%s" +
                        "&pageIndex=%d&pageSize=%d&continuationToken=%s" +
                        "&states=%s&sortOrder=%s&sortField=%s&withCount=%s",
                helloAssoBaseUrl,
                orgSlug,
                formType,
                formSlug,
                from, to,
                userSearchKey,
                pageIndex,
                pageSize,
                continuationToken,
                String.join(",", states),
                sortOrder,
                sortField,
                withCount
        );

        HttpEntity<Void> request = new HttpEntity<>(buildHeaders());
        ResponseEntity<HelloAssoOrdersResponse> resp = restTemplate.exchange(
                url, HttpMethod.GET, request, HelloAssoOrdersResponse.class
        );

        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new RuntimeException("Failed to fetch HelloAsso payments: " + resp.getStatusCode());
        }
        return resp.getBody();
    }

    // Helper to inject the Bearer token header
    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getAccessToken());
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
