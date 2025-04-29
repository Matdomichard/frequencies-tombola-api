package com.frequencies.tombola.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TombolaConfigurationDto {
    private Long id;
    private Integer ticketsPerParticipant;
    private Boolean guaranteeOneLotPerParticipant;
}
