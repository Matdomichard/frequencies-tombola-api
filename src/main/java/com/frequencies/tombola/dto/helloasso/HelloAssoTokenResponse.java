package com.frequencies.tombola.dto.helloasso;

import lombok.*;


@Data
public class HelloAssoTokenResponse {
    private String access_token;
    private String token_type;
    private int expiresIn;
}

