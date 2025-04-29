package com.frequencies.tombola.mapper;

import com.frequencies.tombola.dto.TombolaConfigurationDto;
import com.frequencies.tombola.entity.TombolaConfiguration;

public class TombolaConfigurationMapper {

    public static TombolaConfigurationDto toDto(TombolaConfiguration config) {
        return TombolaConfigurationDto.builder()
                .id(config.getId())
                .ticketsPerParticipant(config.getTicketsPerParticipant())
                .guaranteeOneLotPerParticipant(config.getGuaranteeOneLotPerParticipant())
                .build();
    }

    public static TombolaConfiguration toEntity(TombolaConfigurationDto dto) {
        TombolaConfiguration entity = new TombolaConfiguration();
        entity.setId(dto.getId());
        entity.setTicketsPerParticipant(dto.getTicketsPerParticipant());
        entity.setGuaranteeOneLotPerParticipant(dto.getGuaranteeOneLotPerParticipant());
        return entity;
    }
}
