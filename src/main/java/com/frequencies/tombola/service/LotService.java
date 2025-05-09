// src/main/java/com/frequencies/tombola/service/LotService.java
package com.frequencies.tombola.service;

import com.frequencies.tombola.dto.LotDto;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing Lots (prizes) linked to a Tombola.
 */
public interface LotService {
    /** List all lots across all tombolas */
    List<LotDto> getAllLots();

    /** Fetch a single lot by its ID */
    Optional<LotDto> getLotById(Long id);

    /** Create a new lot (unattached) */
    LotDto createLot(LotDto lotDto);

    /** Update an existing lot */
    Optional<LotDto> updateLot(Long id, LotDto lotDto);

    /** Delete a lot by its ID */
    boolean deleteLot(Long id);

    /** List all lots for a specific tombola */
    List<LotDto> getLotsByTombola(Long tombolaId);

    /** Create a new lot attached to a specific tombola */
    LotDto createLot(Long tombolaId, LotDto dto);

}
