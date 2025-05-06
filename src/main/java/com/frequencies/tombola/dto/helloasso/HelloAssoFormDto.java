package com.frequencies.tombola.dto.helloasso;

import lombok.Data;

@Data
public class HelloAssoFormDto {
    private String currency;
    private String state;
    private String title;
    private String formSlug;
    private String formType;
    private String url;
    private String organizationSlug;
    private Meta meta;

    @Data
    public static class Meta {
        private String createdAt;
        private String updatedAt;
    }
}
