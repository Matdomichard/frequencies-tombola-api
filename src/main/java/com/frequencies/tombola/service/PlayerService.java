package com.frequencies.tombola.service;

import com.frequencies.tombola.dto.PlayerDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PlayerService {

    // List players for a given tombola
    List<PlayerDto> getPlayersByTombola(Long tombolaId);

    // Get a player by their id
    Optional<PlayerDto> getPlayerById(Long id);

    // Create a player for a tombola
    PlayerDto createPlayer(Long tombolaId, PlayerDto dto);

    // Fully update a player
    Optional<PlayerDto> updatePlayer(Long id, PlayerDto dto);

    // Partially update a player
    Optional<PlayerDto> patchPlayer(Long id, Map<String,Object> changes);

    // Delete a player, returns true if deleted, false if not found
    boolean deletePlayer(Long id);
}
