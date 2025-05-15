// src/main/java/com/frequencies/tombola/mapper/LotMapper.java
package com.frequencies.tombola.mapper;

import com.frequencies.tombola.dto.LotDto;
import com.frequencies.tombola.entity.Lot;
import org.springframework.stereotype.Component;

@Component
public class LotMapper {

    /**
     * Map entity → DTO, including assignedToId, status and claimed.
     */
    public LotDto toDto(Lot e) {
        return LotDto.builder()
                .id(e.getId())
                .name(e.getName())
                .donorFirstName(e.getDonorFirstName())
                .donorLastName(e.getDonorLastName())
                .donorCompany(e.getDonorCompany())
                .donorEmail(e.getDonorEmail())
                .donorPhone(e.getDonorPhone())
                .value(e.getValue())
                .imageUrl(e.getImageUrl())
                .status(e.getStatus().name())
                .claimed(e.isClaimed())
                .assignedToId(e.getAssignedTo() != null ? e.getAssignedTo().getId() : null)
                .tombolaId(e.getTombola() != null ? e.getTombola().getId() : null)
                .build();
    }

    /**
     * Map DTO → entity. Tombola and assignedTo associations are handled in service/controller.
     */
    public Lot toEntity(LotDto dto) {
        return Lot.builder()
                .id(dto.getId())
                .name(dto.getName())
                .donorFirstName(dto.getDonorFirstName())
                .donorLastName(dto.getDonorLastName())
                .donorCompany(dto.getDonorCompany())
                .donorEmail(dto.getDonorEmail())
                .donorPhone(dto.getDonorPhone())
                .value(dto.getValue())
                .imageUrl(dto.getImageUrl())
                // status, claimed, assignedTo, tombola handled elsewhere
                .build();
    }
}
