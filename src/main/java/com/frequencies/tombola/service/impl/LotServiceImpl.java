// src/main/java/com/frequencies/tombola/service/impl/LotServiceImpl.java
package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.LotDto;
import com.frequencies.tombola.entity.Lot;
import com.frequencies.tombola.entity.Tombola;
import com.frequencies.tombola.mapper.LotMapper;
import com.frequencies.tombola.repository.LotRepository;
import com.frequencies.tombola.repository.TombolaRepository;
import com.frequencies.tombola.service.LotService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing prizes (lots).
 */
@Service
@RequiredArgsConstructor
public class LotServiceImpl implements LotService {

    private final LotRepository lotRepo;
    private final TombolaRepository tombolaRepo;
    private final LotMapper lotMapper;

    @Override
    public List<LotDto> getAllLots() {
        return lotRepo.findAll().stream()
                .map(lotMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public LotDto createLot(LotDto dto) {
        Lot lot = lotMapper.toEntity(dto);
        Lot saved = lotRepo.save(lot);
        return lotMapper.toDto(saved);
    }

    @Override
    public LotDto createLot(Long tombolaId, LotDto dto) {
        Tombola tombola = tombolaRepo.findById(tombolaId)
                .orElseThrow(() -> new EntityNotFoundException("Tombola not found: " + tombolaId));
        Lot lot = lotMapper.toEntity(dto);
        lot.setTombola(tombola);
        Lot saved = lotRepo.save(lot);
        return lotMapper.toDto(saved);
    }

    @Override
    public List<LotDto> getLotsByTombola(Long tombolaId) {
        return lotRepo.findByTombola_Id(tombolaId).stream()
                .map(lot -> LotDto.builder()
                        .id(lot.getId())
                        .name(lot.getName())
                        .name(lot.getName())
                        .description(lot.getDescription())
                        .donorName(lot.getDonorName())
                        .donorContact(lot.getDonorContact())
                        .value(lot.getValue())
                        .imageUrl(lot.getImageUrl())
                        .status(lot.getStatus())               // <-- copier le status
                        .claimed(lot.isClaimed())              // <-- copier claimed
                        .assignedToId(
                                lot.getAssignedTo() != null
                                        ? lot.getAssignedTo().getId()
                                        : null
                        )                                      // <-- copier l’ID du joueur
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public java.util.Optional<LotDto> updateLot(Long id, LotDto dto) {
        return lotRepo.findById(id)
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setDescription(dto.getDescription());
                    existing.setDonorName(dto.getDonorName());
                    existing.setDonorContact(dto.getDonorContact());
                    existing.setValue(dto.getValue());
                    existing.setImageUrl(dto.getImageUrl());
                    return lotRepo.save(existing);
                })
                .map(lotMapper::toDto);
    }

    @Override
    public boolean deleteLot(Long id) {
        if (!lotRepo.existsById(id)) {
            return false;
        }
        lotRepo.deleteById(id);
        return true;
    }

    @Override
    public java.util.Optional<LotDto> getLotById(Long id) {
        return lotRepo.findById(id)
                .map(lotMapper::toDto);
    }

}
