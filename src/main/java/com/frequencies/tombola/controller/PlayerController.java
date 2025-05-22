package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for direct access to individual Player resources.
 * No global creation or listing: players are always created within a Tombola context.
 */
@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    /**
     * Get player by ID.
     * @param id Player ID
     * @return 200 OK with PlayerDto or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Full update of a player.
     * @param id Player ID
     * @param dto Updated player data
     * @return 200 OK with updated PlayerDto or 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlayerDto> updatePlayer(
            @PathVariable Long id,
            @Valid @RequestBody PlayerDto dto
    ) {
        return playerService.updatePlayer(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Partial update of a player (PATCH).
     * @param id Player ID
     * @param changes Map of fields to update
     * @return 200 OK with updated PlayerDto or 404 Not Found
     */
    @PatchMapping("/{id}")
    public ResponseEntity<PlayerDto> patchPlayer(
            @PathVariable Long id,
            @RequestBody Map<String, Object> changes
    ) {
        return playerService.patchPlayer(id, changes)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete player by ID.
     * @param id Player ID
     * @return 204 No Content if deleted, 404 Not Found otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        boolean deleted = playerService.deletePlayer(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
