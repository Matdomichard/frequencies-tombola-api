package com.frequencies.tombola.mapper;

import com.frequencies.tombola.dto.helloasso.HelloAssoFormIdDto;
import com.frequencies.tombola.entity.HelloAssoFormId;
import com.frequencies.tombola.entity.Tombola;
import org.springframework.stereotype.Component;

@Component
public class HelloAssoFormIdMapper {

    public HelloAssoFormIdDto toDto(HelloAssoFormId entity) {
        return HelloAssoFormIdDto.builder()
                .id(entity.getId())
                .tombolaId(entity.getTombola().getId())
                .formSlug(entity.getFormSlug())
                .formType(entity.getFormType())
                .build();
    }

    public HelloAssoFormId toEntity(HelloAssoFormIdDto dto, Tombola tombola) {
        return HelloAssoFormId.builder()
                .id(dto.getId())
                .tombola(tombola)
                .formSlug(dto.getFormSlug())
                .formType(dto.getFormType())
                .build();
    }
}
