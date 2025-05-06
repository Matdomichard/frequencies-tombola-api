package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.entity.Player;
import com.frequencies.tombola.entity.Tombola;
import com.frequencies.tombola.mapper.PlayerMapper;
import com.frequencies.tombola.repository.PlayerRepository;
import com.frequencies.tombola.repository.TombolaRepository;
import com.frequencies.tombola.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TombolaRepository tombolaRepository;
    private final PlayerMapper playerMapper;   // ← instance injectée

    @Override
    public List<PlayerDto> getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(playerMapper::toDto)   // appel d’instance
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerDto> getPlayersByTombola(Long tombolaId) {
        return playerRepository.findByTombola_Id(tombolaId).stream()
                .map(playerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PlayerDto> getPlayerById(Long id) {
        return playerRepository.findById(id)
                .map(playerMapper::toDto);
    }

    @Override
    public PlayerDto createPlayer(Long tombolaId, PlayerDto dto) {
        Tombola tombola = tombolaRepository.findById(tombolaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tombola not found"));
        Player entity = playerMapper.toEntity(dto);
        entity.setTombola(tombola);
        Player saved = playerRepository.save(entity);
        return playerMapper.toDto(saved);
    }

    @Override
    public Optional<PlayerDto> updatePlayer(Long id, PlayerDto dto) {
        return playerRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(dto.getFirstName());
                    existing.setLastName(dto.getLastName());
                    existing.setEmail(dto.getEmail());
                    existing.setPhoneNumber(dto.getPhone());
                    existing.setHasCollectedPrize(dto.isHasCollectedPrize());
                    return playerRepository.save(existing);
                })
                .map(playerMapper::toDto);
    }

    @Override
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }
}
