package com.frequencies.tombola.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TombolaDto {
    private Long id;
    private String name;
    private String helloAssoFormSlug;
    private LocalDateTime createdAt;
}
