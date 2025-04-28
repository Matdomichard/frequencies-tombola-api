package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.entity.Player;
import com.frequencies.tombola.mapper.PlayerMapper;
import com.frequencies.tombola.repository.PlayerRepository;
import com.frequencies.tombola.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public List<PlayerDto> getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(PlayerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PlayerDto> getPlayerById(Long id) {
        return playerRepository.findById(id)
                .map(PlayerMapper::toDto);
    }

    @Override
    public PlayerDto createPlayer(PlayerDto playerDto) {
        Player player = PlayerMapper.toEntity(playerDto);
        Player savedPlayer = playerRepository.save(player);
        return PlayerMapper.toDto(savedPlayer);
    }

    @Override
    public Optional<PlayerDto> updatePlayer(Long id, PlayerDto playerDto) {
        return playerRepository.findById(id)
                .map(existingPlayer -> {
                    existingPlayer.setName(playerDto.getName());
                    existingPlayer.setEmail(playerDto.getEmail());
                    existingPlayer.setPhoneNumber(playerDto.getPhoneNumber());
                    existingPlayer.setHasCollectedPrize(playerDto.isHasCollectedPrize());
                    return PlayerMapper.toDto(playerRepository.save(existingPlayer));
                });
    }

    @Override
    public boolean deletePlayer(Long id) {
        if (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
