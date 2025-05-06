package com.frequencies.tombola.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TombolaDto {
    private Long id;
    private String name;

    /** whether the tombola is active; default to true */
    @Builder.Default
    private boolean active = true;

    /** the HelloAsso form “type” (e.g. Event, PaymentForm…) */
    @Builder.Default
    private String helloAssoFormType = "Event";

    /** the HelloAsso form slug (e.g. tombola-1) */
    private String helloAssoFormSlug;

    /** timestamp when this tombola was created */
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
