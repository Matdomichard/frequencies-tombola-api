// src/main/java/com/frequencies/tombola/mapper/LotMapper.java
package com.frequencies.tombola.mapper;

import com.frequencies.tombola.dto.LotDto;
import com.frequencies.tombola.entity.Lot;
import org.springframework.stereotype.Component;

@Component
public class LotMapper {

    /**
     * Map entity → DTO, en incluant assignedToId, status et claimed.
     */
    public LotDto toDto(Lot e) {
        return LotDto.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .donorName(e.getDonorName())
                .donorContact(e.getDonorContact())
                .value(e.getValue())
                .imageUrl(e.getImageUrl())
                .status(e.getStatus())
                .claimed(e.isClaimed())
                .assignedToId(e.getAssignedTo() != null ? e.getAssignedTo().getId() : null)
                .build();
    }


    /**
     * Map DTO → entity.  Les associations tombola et assignedTo
     * sont gérées en service/controller.
     */
    public Lot toEntity(LotDto dto) {
        return Lot.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .donorName(dto.getDonorName())
                .donorContact(dto.getDonorContact())
                .value(dto.getValue())
                .imageUrl(dto.getImageUrl())
                // on n'affecte pas status ni claimed ni assignedTo ici :
                .build();
    }
}
