package com.frequencies.tombola.service;

import com.frequencies.tombola.dto.PlayerDto;

import java.util.List;
import java.util.Optional;

public interface PlayerService {
    List<PlayerDto> getAllPlayers();
    Optional<PlayerDto> getPlayerById(Long id);
    PlayerDto createPlayer(PlayerDto playerDto);
    Optional<PlayerDto> updatePlayer(Long id, PlayerDto playerDto);
    boolean deletePlayer(Long id);
}
