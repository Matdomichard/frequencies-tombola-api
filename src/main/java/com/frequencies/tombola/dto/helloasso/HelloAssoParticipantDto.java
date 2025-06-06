package com.frequencies.tombola.dto.helloasso;

import com.frequencies.tombola.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelloAssoParticipantDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String state; // paid, canceled, etc.
    private Integer ticketNumber;
    private PaymentMethod paymentMethod; // CARD or CASH
}
