package com.frequencies.tombola.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Service to authenticate with HelloAsso API using client credentials.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HelloAssoAuthServiceImpl implements HelloAssoAuthService {

    private final RestTemplate restTemplate;

    @Value("${helloasso.api-url}")
    private String apiUrl;

    @Value("${helloasso.client-id}")
    private String clientId;

    @Value("${helloasso.client-secret}")
    private String clientSecret;

    private String cachedToken;
    private long tokenExpirationTime = 0;

    @Override
    public String getAccessToken() {
        if (cachedToken != null && System.currentTimeMillis() < tokenExpirationTime) {
            log.info("Réponse token: {}", cachedToken);
            return cachedToken;
        }

        String tokenUrl = apiUrl + "/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("grant_type", "client_credentials");
        bodyMap.add("client_id", clientId);
        bodyMap.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(bodyMap, headers);

        try {
            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(tokenUrl, request, TokenResponse.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                cachedToken = response.getBody().getAccessToken();
                tokenExpirationTime = System.currentTimeMillis() + (response.getBody().getExpires_in() * 1000);
                return cachedToken;
            } else {
                throw new RuntimeException("Failed to fetch HelloAsso token: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Failed to get HelloAsso token", e);
            throw new RuntimeException("HelloAsso auth failed", e);
        }
    }

    /**
     * Internal class to deserialize the token response.
     */
    @Slf4j
    @lombok.Data
    private static class TokenResponse {
        private String access_token;
        private String token_type;
        private int expires_in;

        public String getAccessToken() {
            return access_token;
        }
    }
}
