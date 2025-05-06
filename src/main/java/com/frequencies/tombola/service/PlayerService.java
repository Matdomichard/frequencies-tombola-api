package com.frequencies.tombola.service;

import com.frequencies.tombola.dto.PlayerDto;
import java.util.List;
import java.util.Optional;

public interface PlayerService {
    // liste tous les joueurs
    List<PlayerDto> getAllPlayers();

    // liste les joueurs d’une tombola donnée
    List<PlayerDto> getPlayersByTombola(Long tombolaId);

    Optional<PlayerDto> getPlayerById(Long id);

    PlayerDto createPlayer(Long tombolaId, PlayerDto dto);

    Optional<PlayerDto> updatePlayer(Long id, PlayerDto dto);

    void deletePlayer(Long id);
}
