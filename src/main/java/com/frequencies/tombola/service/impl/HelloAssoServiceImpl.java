package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.config.HelloAssoProperties;
import com.frequencies.tombola.dto.helloasso.*;
import com.frequencies.tombola.service.HelloAssoService;
import com.frequencies.tombola.service.auth.HelloAssoAuthService;
import lombok.RequiredArgsConstructor;
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
        // 1) get your token + build headers
        HttpHeaders headers = authHeaders();

        // 2) build the URL with from/to wide window
        Instant now = Instant.now();
        String from = now.minus(30, ChronoUnit.DAYS).toString();
        String to   = now.plus(1, ChronoUnit.DAYS).toString();

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

        return resp.getBody().getData().stream()
                .map(o -> {
                    HelloAssoPayer p = o.getPayer();
                    return HelloAssoParticipantDto.builder()
                            .firstName(p.getFirstName())
                            .lastName( p.getLastName())
                            .email(    p.getEmail())
                            .phone(    p.getPhone())
                            .state(    o.getState())
                            .build();
                })
                .collect(Collectors.toList());

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
                .queryParam("to",   to.toString())
                .queryParam("userSearchKey", userSearchKey)
                .queryParam("pageIndex", pageIndex)
                .queryParam("pageSize",  pageSize)
                .queryParam("continuationToken", continuationToken)
                .queryParam("sortOrder", sortOrder)
                .queryParam("sortField", sortField)
                .queryParam("withCount", withCount);
        // only add states if non-null
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
        return resp.getBody();
    }

}
