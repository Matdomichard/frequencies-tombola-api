package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.LotDto;
import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.dto.TombolaDto;
import com.frequencies.tombola.service.LotService;
import com.frequencies.tombola.service.PlayerService;
import com.frequencies.tombola.service.TombolaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Tombola management.
 */
@Slf4j
@RestController
@RequestMapping("/tombolas")
@RequiredArgsConstructor
public class TombolaController {

    private final TombolaService tombolaService;
    private final PlayerService playerService;
    private final LotService lotService;

    /** GET /tombolas - Get all tombolas */
    @GetMapping
    public ResponseEntity<List<TombolaDto>> getAll() {
        log.info("GET /tombolas");
        return ResponseEntity.ok(tombolaService.getAll());
    }

    /** GET /tombolas/{id} - Get a tombola by its id */
    @GetMapping("/{id}")
    public ResponseEntity<TombolaDto> getById(@PathVariable Long id) {
        log.info("GET /tombolas/{}", id);
        Optional<TombolaDto> tombola = tombolaService.getById(id);
        return tombola.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** POST /tombolas - Create a new tombola */
    @PostMapping
    public ResponseEntity<TombolaDto> create(@Valid @RequestBody TombolaDto dto) {
        log.info("POST /tombolas → name={}", dto.getName());
        TombolaDto created = tombolaService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    /** DELETE /tombolas/{id} - Delete a tombola */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /tombolas/{}", id);
        boolean deleted = tombolaService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // --- players routes ---

    /** GET /tombolas/{id}/players - Get players by tombola */
    @GetMapping("/{id}/players")
    public ResponseEntity<List<PlayerDto>> getPlayers(@PathVariable("id") Long tombolaId) {
        log.info("GET /tombolas/{}/players", tombolaId);
        return ResponseEntity.ok(playerService.getPlayersByTombola(tombolaId));
    }

    /** POST /tombolas/{id}/players - Add player to tombola */
    @PostMapping("/{id}/players")
    public ResponseEntity<PlayerDto> createPlayer(
            @PathVariable("id") Long tombolaId,
            @Valid @RequestBody PlayerDto playerDto
    ) {
        log.info("POST /tombolas/{}/players", tombolaId);
        PlayerDto created = playerService.createPlayer(tombolaId, playerDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{playerId}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    // --- lots routes ---

    /** GET /tombolas/{id}/lots - Get lots by tombola */
    @GetMapping("/{id}/lots")
    public ResponseEntity<List<LotDto>> getLots(@PathVariable("id") Long tombolaId) {
        log.info("GET /tombolas/{}/lots", tombolaId);
        return ResponseEntity.ok(lotService.getLotsByTombola(tombolaId));
    }

    /** POST /tombolas/{id}/lots - Add lot to tombola */
    @PostMapping("/{id}/lots")
    public ResponseEntity<LotDto> createLot(
            @PathVariable("id") Long tombolaId,
            @Valid @RequestBody LotDto dto
    ) {
        log.info("POST /tombolas/{}/lots → name={}", tombolaId, dto.getName());
        LotDto created = lotService.createLot(tombolaId, dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{lotId}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }
}
