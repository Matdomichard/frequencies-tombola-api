package com.frequencies.tombola.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LotDto {
    private Long id;

    @NotBlank(message = "Le nom du lot est obligatoire")
    private String name;

    private String donorFirstName;
    private String donorLastName;
    private String donorCompany;

    @Email(message = "Email du donateur invalide")
    private String donorEmail;

    private String donorPhone;

    @DecimalMin(value = "0.0", inclusive = true, message = "La valeur doit être positive")
    private BigDecimal value;

    private String imageUrl;
    private String status;
    private Boolean claimed;
    private Long assignedToId;

    @NotNull(message = "Le tombolaId est obligatoire")
    private Long tombolaId;
}
