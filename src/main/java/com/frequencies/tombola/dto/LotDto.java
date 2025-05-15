// src/main/java/com/frequencies/tombola/dto/LotDto.java
package com.frequencies.tombola.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LotDto {
    private Long id;
    private String name;
    private String donorFirstName;
    private String donorLastName;
    private String donorCompany;
    private String donorEmail;
    private String donorPhone;
    private BigDecimal value;
    private String imageUrl;
    private String status;
    private Boolean claimed;
    private Long assignedToId;
    private Long tombolaId;
}
