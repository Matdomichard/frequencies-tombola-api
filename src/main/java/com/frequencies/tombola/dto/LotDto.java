package com.frequencies.tombola.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LotDto {
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    private String description;
}
