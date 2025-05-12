package com.frequencies.tombola.dto;

import com.frequencies.tombola.enums.PaymentMethod;
import lombok.*;

import java.util.List;

// src/main/java/com/frequencies/tombola/dto/PlayerDto.java
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PlayerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer ticketNumber;
    private Long assignedPrizeId;
    private boolean hasCollectedPrize;
    private boolean emailSent;
    private PaymentMethod paymentMethod;
    private List<LotDto> assignedLots;
}
