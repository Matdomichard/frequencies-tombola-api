package com.frequencies.tombola.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private boolean hasCollectedPrize;
}
