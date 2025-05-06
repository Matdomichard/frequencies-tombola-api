package com.frequencies.tombola.mapper;

import com.frequencies.tombola.dto.TombolaDto;
import com.frequencies.tombola.entity.Tombola;
import org.springframework.stereotype.Component;

@Component
public class TombolaMapper {

    public Tombola toEntity(TombolaDto dto) {
        return Tombola.builder()
                .id(dto.getId())
                .name(dto.getName())
                .helloAssoFormSlug(dto.getHelloAssoFormSlug())
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .helloAssoFormType(dto.getHelloAssoFormType())
                .build();
    }

    public TombolaDto toDto(Tombola entity) {
        return TombolaDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .helloAssoFormSlug(entity.getHelloAssoFormSlug())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .helloAssoFormType(entity.getHelloAssoFormType())
                .build();
    }
}
