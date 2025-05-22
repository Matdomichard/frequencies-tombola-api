package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.LotDto;
import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.entity.Lot;
import com.frequencies.tombola.entity.Player;
import com.frequencies.tombola.entity.Tombola;
import com.frequencies.tombola.mapper.LotMapper;
import com.frequencies.tombola.mapper.PlayerMapper;
import com.frequencies.tombola.repository.LotRepository;
import com.frequencies.tombola.repository.PlayerRepository;
import com.frequencies.tombola.repository.TombolaRepository;
import com.frequencies.tombola.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TombolaRepository tombolaRepository;
    private final LotRepository lotRepository;
    private final PlayerMapper playerMapper;
    private final LotMapper lotMapper;

    @Override
    public List<PlayerDto> getPlayersByTombola(Long tombolaId) {
        return playerRepository.findByTombolaId(tombolaId).stream()
                .map(this::toDtoWithExtras)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PlayerDto> getPlayerById(Long id) {
        return playerRepository.findById(id)
                .map(this::toDtoWithExtras);
    }

    @Override
    public PlayerDto createPlayer(Long tombolaId, PlayerDto dto) {
        Tombola tombola = tombolaRepository.findById(tombolaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tombola not found"));
        Player entity = playerMapper.toEntity(dto);
        entity.setTombola(tombola);
        Player saved = playerRepository.save(entity);
        return toDtoWithExtras(saved);
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
                    Player saved = playerRepository.save(existing);
                    return toDtoWithExtras(saved);
                });
    }

    @Override
    public Optional<PlayerDto> patchPlayer(Long id, Map<String, Object> changes) {
        return playerRepository.findById(id)
                .map(existing -> {
                    if (changes.containsKey("firstName")) {
                        existing.setFirstName((String) changes.get("firstName"));
                    }
                    if (changes.containsKey("lastName")) {
                        existing.setLastName((String) changes.get("lastName"));
                    }
                    if (changes.containsKey("email")) {
                        existing.setEmail((String) changes.get("email"));
                    }
                    if (changes.containsKey("phone")) {
                        existing.setPhoneNumber((String) changes.get("phone"));
                    }
                    if (changes.containsKey("hasCollectedPrize")) {
                        existing.setHasCollectedPrize((Boolean) changes.get("hasCollectedPrize"));
                    }
                    Player updated = playerRepository.save(existing);
                    return toDtoWithExtras(updated);
                });
    }

    @Override
    public boolean deletePlayer(Long id) {
        if (!playerRepository.existsById(id)) {
            return false;
        }
        playerRepository.deleteById(id);
        return true;
    }

    /**
     * Enrich the PlayerDto with ticketNumber and assignedLots.
     */
    private PlayerDto toDtoWithExtras(Player player) {
        PlayerDto dto = playerMapper.toDto(player);
        dto.setTicketNumber(player.getTicketNumber());

        // Find all lots assigned to this player
        List<Lot> lots = lotRepository.findByAssignedTo_Id(player.getId());
        List<LotDto> lotDtos = lots.stream()
                .map(lotMapper::toDto)
                .collect(Collectors.toList());
        dto.setAssignedLots(lotDtos);

        return dto;
    }
}
