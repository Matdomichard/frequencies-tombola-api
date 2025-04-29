package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.config.HelloAssoProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class HelloAssoService {

    private final HelloAssoProperties helloAssoProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    private String accessToken;

    /**
     * Authenticate with HelloAsso API using client credentials flow.
     */
    public void authenticate() {
        if (this.accessToken != null) {
            return; // Token already available
        }

        String tokenUrl = helloAssoProperties.getApiUrl() + "/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "grant_type=client_credentials&client_id=" + helloAssoProperties.getClientId() +
                "&client_secret=" + helloAssoProperties.getClientSecret();

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                entity,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            this.accessToken = (String) response.getBody().get("access_token");
            log.info("HelloAsso authentication successful");
        } else {
            throw new RuntimeException("Failed to authenticate with HelloAsso API");
        }
    }

    /**
     * Retrieve the list of forms from HelloAsso.
     * @return JSON response as a String
     */
    public String getForms() {
        authenticate();

        String url = helloAssoProperties.getApiUrl() + "/v5/organizations/" + helloAssoProperties.getOrganizationSlug() + "/forms";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch forms from HelloAsso");
        }
    }
}
