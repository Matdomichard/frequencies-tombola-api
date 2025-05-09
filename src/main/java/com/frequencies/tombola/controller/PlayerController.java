package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    /** GET /players */
    @GetMapping
    public ResponseEntity<List<PlayerDto>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    /** GET /players/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** PUT /players/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<PlayerDto> updatePlayer(
            @PathVariable Long id,
            @RequestBody PlayerDto dto
    ) {
        return playerService.updatePlayer(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** DELETE /players/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    /** PATCH /players/{id} */
    @PatchMapping("/{id}")
    public ResponseEntity<PlayerDto> patchPlayer(
            @PathVariable Long id,
            @RequestBody Map<String,Object> changes
    ) {
        return playerService.patchPlayer(id, changes)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
