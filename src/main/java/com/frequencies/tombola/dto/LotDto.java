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
public class LotDto {
    private Long id;
    private String name;
    private String description;
    private String donorName;
    private String donorContact;
    private BigDecimal value;
    private String imageUrl;
    private LotStatus status;
    private boolean claimed;
    private Long assignedToId;
}
