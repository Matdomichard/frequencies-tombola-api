package com.frequencies.tombola.mapper;

import com.frequencies.tombola.dto.LotDto;
import com.frequencies.tombola.entity.Lot;

public class LotMapper {

    public static LotDto toDto(Lot lot) {
        return LotDto.builder()
                .id(lot.getId())
                .name(lot.getName())
                .description(lot.getDescription())
                .build();
    }

    public static Lot toEntity(LotDto lotDto) {
        return Lot.builder()
                .id(lotDto.getId())
                .name(lotDto.getName())
                .description(lotDto.getDescription())
                .build();
    }
}
