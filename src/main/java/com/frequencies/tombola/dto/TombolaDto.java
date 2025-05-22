package com.frequencies.tombola.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TombolaDto {
    private Long id;

    @NotBlank(message = "Tombola name is mandatory")
    private String name;

    @Builder.Default
    private boolean active = true;

    @NotBlank(message = "HelloAsso form type is required")
    @Builder.Default
    private String helloAssoFormType = "Event";

    @NotBlank(message = "HelloAsso form slug is required")
    private String helloAssoFormSlug;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
