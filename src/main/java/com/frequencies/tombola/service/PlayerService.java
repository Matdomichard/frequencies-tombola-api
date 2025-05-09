package com.frequencies.tombola.service;

import com.frequencies.tombola.dto.PlayerDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PlayerService {
    // liste tous les joueurs
    List<PlayerDto> getAllPlayers();

    // liste les joueurs d’une tombola donnée
    List<PlayerDto> getPlayersByTombola(Long tombolaId);

    // récupère un joueur par son id
    Optional<PlayerDto> getPlayerById(Long id);

    // crée un joueur pour une tombola
    PlayerDto createPlayer(Long tombolaId, PlayerDto dto);

    // met à jour entièrement un joueur
    Optional<PlayerDto> updatePlayer(Long id, PlayerDto dto);

    // applique partiellement des changements sur un joueur
    Optional<PlayerDto> patchPlayer(Long id, Map<String,Object> changes);

    // supprime un joueur
    void deletePlayer(Long id);
}
