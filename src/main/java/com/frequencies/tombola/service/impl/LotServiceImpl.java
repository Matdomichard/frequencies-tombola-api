package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.LotDto;
import com.frequencies.tombola.entity.Lot;
import com.frequencies.tombola.mapper.LotMapper;
import com.frequencies.tombola.repository.LotRepository;
import com.frequencies.tombola.service.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LotServiceImpl implements LotService {

    @Autowired
    private LotRepository lotRepository;

    @Override
    public List<LotDto> getAllLots() {
        return lotRepository.findAll().stream()
                .map(LotMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LotDto> getLotById(Long id) {
        return lotRepository.findById(id)
                .map(LotMapper::toDto);
    }

    @Override
    public LotDto createLot(LotDto lotDto) {
        Lot lot = LotMapper.toEntity(lotDto);
        Lot savedLot = lotRepository.save(lot);
        return LotMapper.toDto(savedLot);
    }

    @Override
    public Optional<LotDto> updateLot(Long id, LotDto lotDto) {
        return lotRepository.findById(id)
                .map(existingLot -> {
                    existingLot.setName(lotDto.getName());
                    existingLot.setDescription(lotDto.getDescription());
                    Lot updatedLot = lotRepository.save(existingLot);
                    return LotMapper.toDto(updatedLot);
                });
    }

    @Override
    public boolean deleteLot(Long id) {
        if (lotRepository.existsById(id)) {
            lotRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
