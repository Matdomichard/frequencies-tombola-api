package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.helloasso.HelloAssoParticipantDto;
import com.frequencies.tombola.dto.helloasso.HelloAssoOrder;
import com.frequencies.tombola.dto.helloasso.HelloAssoOrdersResponse;
import com.frequencies.tombola.dto.helloasso.HelloAssoPayer;
import com.frequencies.tombola.service.HelloAssoService;
import com.frequencies.tombola.service.auth.HelloAssoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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
        String url = helloAssoBaseUrl + "/v5/organizations/" + orgSlug + "/" + formType + "/" + formSlug + "/orders";

        ResponseEntity<HelloAssoOrdersResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(buildHeaders()),
                HelloAssoOrdersResponse.class
        );

        List<HelloAssoParticipantDto> paidParticipants = new ArrayList<>();

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            for (HelloAssoOrder order : response.getBody().getData()) {
                if ("paid".equalsIgnoreCase(order.getState())) {
                    for (HelloAssoPayer payer : order.getPayers()) {
                        paidParticipants.add(HelloAssoParticipantDto.builder()
                                .firstName(payer.getFirstName())
                                .lastName(payer.getLastName())
                                .email(payer.getEmail())
                                .phone(payer.getPhone())
                                .state(order.getState())
                                .build());
                    }
                }
            }
        }

        return paidParticipants;
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getAccessToken());
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
