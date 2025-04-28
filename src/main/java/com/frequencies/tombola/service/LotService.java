package com.frequencies.tombola.service;

import com.frequencies.tombola.dto.LotDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface LotService {
    List<LotDto> getAllLots();
    Optional<LotDto> getLotById(Long id);
    LotDto createLot(LotDto lotDto);
    Optional<LotDto> updateLot(Long id, LotDto lotDto);
    boolean deleteLot(Long id);
}
