package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.TombolaDto;
import com.frequencies.tombola.service.TombolaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tombolas")
@RequiredArgsConstructor
public class TombolaController {

    private final TombolaService service;

    @GetMapping
    public List<TombolaDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TombolaDto> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TombolaDto create(@RequestBody TombolaDto dto) {
        return service.create(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
