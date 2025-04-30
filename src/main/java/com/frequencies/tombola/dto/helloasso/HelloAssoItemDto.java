package com.frequencies.tombola.dto.helloasso;

import lombok.Data;

@Data
public class HelloAssoItemDto {
    private Long id;
    private String payerFirstName;
    private String payerLastName;
    private String payerEmail;
    private Double amount;
    private String status;
}
