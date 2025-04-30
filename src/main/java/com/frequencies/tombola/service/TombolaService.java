package com.frequencies.tombola.service;

import com.frequencies.tombola.dto.TombolaDto;

import java.util.List;
import java.util.Optional;

public interface TombolaService {
    List<TombolaDto> getAll();
    Optional<TombolaDto> getById(Long id);
    TombolaDto create(TombolaDto dto);
    void delete(Long id);
}
