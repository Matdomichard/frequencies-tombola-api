package com.frequencies.tombola.dto;

import com.frequencies.tombola.enums.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDto {
    private Long id;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Phone is mandatory")
    private String phone;

    @NotNull(message = "Ticket number is required")
    @Min(value = 1, message = "Ticket number must be at least 1")
    private Integer ticketNumber;

    private Long assignedPrizeId;

    private boolean hasCollectedPrize;

    private boolean emailSent;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private List<LotDto> assignedLots;
}
