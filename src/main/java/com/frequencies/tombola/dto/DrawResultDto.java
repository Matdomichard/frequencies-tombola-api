// src/main/java/com/frequencies/tombola/dto/DrawResultDto.java
package com.frequencies.tombola.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrawResultDto {
    private List<PlayerDto> players;
    private List<LotDto>    lots;
}
