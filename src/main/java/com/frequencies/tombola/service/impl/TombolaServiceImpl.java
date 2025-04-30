package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.TombolaDto;
import com.frequencies.tombola.mapper.TombolaMapper;
import com.frequencies.tombola.repository.TombolaRepository;
import com.frequencies.tombola.service.TombolaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TombolaServiceImpl implements TombolaService {

    private final TombolaRepository repository;
    private final TombolaMapper mapper;

    @Override
    public List<TombolaDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TombolaDto> getById(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public TombolaDto create(TombolaDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
