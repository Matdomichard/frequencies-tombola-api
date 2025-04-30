package com.frequencies.tombola.service;

import com.frequencies.tombola.dto.helloasso.HelloAssoFormIdDto;

import java.util.Optional;

public interface HelloAssoFormIdService {
    HelloAssoFormIdDto save(HelloAssoFormIdDto dto);
    Optional<HelloAssoFormIdDto> findByTombolaId(Long tombolaId);
}
