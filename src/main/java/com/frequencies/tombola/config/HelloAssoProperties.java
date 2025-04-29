package com.frequencies.tombola.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "helloasso")
public class HelloAssoProperties {
    private String clientId;
    private String clientSecret;
    private String apiUrl = "https://api.helloasso.com"; // Valeur par défaut
    private String organizationSlug;
}
