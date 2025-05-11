package com.frequencies.tombola.dto;

import com.frequencies.tombola.entity.LotStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private Double shareAmount;
    private Double shareItemAmount;
    private Double shareOptionsAmount;
    private Long id;
    private Double amount;
    private String type;
    private Double initialAmount;
    private String state;
    private String name;
    private Integer quantity;
}
