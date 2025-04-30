package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.helloasso.HelloAssoFormIdDto;
import com.frequencies.tombola.entity.Tombola;
import com.frequencies.tombola.mapper.HelloAssoFormIdMapper;
import com.frequencies.tombola.repository.HelloAssoFormIdRepository;
import com.frequencies.tombola.repository.TombolaRepository;
import com.frequencies.tombola.service.HelloAssoFormIdService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HelloAssoFormIdServiceImpl implements HelloAssoFormIdService {

    private final HelloAssoFormIdRepository repository;
    private final TombolaRepository tombolaRepository;
    private final HelloAssoFormIdMapper mapper;

    public HelloAssoFormIdServiceImpl(HelloAssoFormIdRepository repository, TombolaRepository tombolaRepository, HelloAssoFormIdMapper mapper) {
        this.repository = repository;
        this.tombolaRepository = tombolaRepository;
        this.mapper = mapper;
    }

    @Override
    public HelloAssoFormIdDto save(HelloAssoFormIdDto dto) {
        Tombola tombola = tombolaRepository.findById(dto.getTombolaId())
                .orElseThrow(() -> new EntityNotFoundException("Tombola not found"));
        var entity = mapper.toEntity(dto, tombola);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public Optional<HelloAssoFormIdDto> findByTombolaId(Long tombolaId) {
        return tombolaRepository.findById(tombolaId)
                .flatMap(repository::findByTombola)
                .map(mapper::toDto);
    }
}
