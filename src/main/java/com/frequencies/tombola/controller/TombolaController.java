package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.dto.TombolaDto;
import com.frequencies.tombola.service.PlayerService;
import com.frequencies.tombola.service.TombolaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tombolas")
@RequiredArgsConstructor
public class TombolaController {

    private final TombolaService tombolaService;
    private final PlayerService playerService;

    /** GET /tombolas */
    @GetMapping
    public ResponseEntity<List<TombolaDto>> getAll() {
        log.info("GET /tombolas");
        return ResponseEntity.ok(tombolaService.getAll());
    }

    /** GET /tombolas/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<TombolaDto> getById(@PathVariable Long id) {
        log.info("GET /tombolas/{}", id);
        return tombolaService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** POST /tombolas */
    @PostMapping
    public ResponseEntity<TombolaDto> create(@RequestBody TombolaDto dto) {
        log.info("POST /tombolas → name={}", dto.getName());
        return ResponseEntity.ok(tombolaService.create(dto));
    }

    /** DELETE /tombolas/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /tombolas/{}", id);
        tombolaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- tout ce qui concerne les joueurs d’une tombola : ---

    /** GET /tombolas/{id}/players */
    @GetMapping("/{id}/players")
    public ResponseEntity<List<PlayerDto>> getPlayers(@PathVariable("id") Long tombolaId) {
        log.info("GET /tombolas/{}/players", tombolaId);
        return ResponseEntity.ok(playerService.getPlayersByTombola(tombolaId));
    }

    /** POST /tombolas/{id}/players */
    @PostMapping("/{id}/players")
    public ResponseEntity<PlayerDto> createPlayer(
            @PathVariable("id") Long tombolaId,
            @RequestBody PlayerDto playerDto
    ) {
        log.info("POST /tombolas/{}/players", tombolaId);
        return ResponseEntity.ok(playerService.createPlayer(tombolaId, playerDto));
    }
}
