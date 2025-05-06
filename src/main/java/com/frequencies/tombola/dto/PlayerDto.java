package com.frequencies.tombola.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean hasCollectedPrize;
}
