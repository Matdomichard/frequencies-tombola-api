package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.dto.TombolaDto;
import com.frequencies.tombola.dto.helloasso.HelloAssoParticipantDto;
import com.frequencies.tombola.entity.Player;
import com.frequencies.tombola.entity.Tombola;
import com.frequencies.tombola.enums.PaymentMethod;
import com.frequencies.tombola.mapper.PlayerMapper;
import com.frequencies.tombola.mapper.TombolaMapper;
import com.frequencies.tombola.repository.PlayerRepository;
import com.frequencies.tombola.repository.TombolaRepository;
import com.frequencies.tombola.service.HelloAssoService;
import com.frequencies.tombola.service.PlayerService;
import com.frequencies.tombola.service.TombolaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TombolaServiceImpl implements TombolaService {

    private final TombolaRepository tombolaRepository;
    private final PlayerRepository   playerRepository;
    private final PlayerMapper       playerMapper;
    private final TombolaMapper      tombolaMapper;
    private final HelloAssoService   helloAssoService;
    private final PlayerService  playerService;

    @Override
    public TombolaDto create(TombolaDto dto) {
        // 1) map DTO → entity and save
        Tombola saved = tombolaRepository.save(tombolaMapper.toEntity(dto));

        // 2) pull all participants (paid and free) from HelloAsso
        List<HelloAssoParticipantDto> participants = helloAssoService.getAllParticipants(
                dto.getHelloAssoFormType(),
                dto.getHelloAssoFormSlug()
        );

        log.info("participants --> {}", participants);

        // 3) map & save them as Player entities
        List<Player> players = participants.stream()
                .map(p -> Player.builder()
                        .firstName(   p.getFirstName())
                        .lastName(    p.getLastName())
                        .email(       p.getEmail())
                        .phoneNumber( p.getPhone())
                        .ticketNumber(p.getTicketNumber())
                        .paymentMethod(p.getPaymentMethod())
                        .tombola(     saved)
                        .build())
                .collect(Collectors.toList());

        playerRepository.saveAll(players);

        // 4) return the newly‐created tombola as a DTO
        return tombolaMapper.toDto(saved);
    }

    @Override
    public List<TombolaDto> getAll() {
        return tombolaRepository.findAll()
                .stream()
                .map(tombolaMapper::toDto)
                .toList();
    }

    @Override
    public Optional<TombolaDto> getById(Long id) {
        return tombolaRepository.findById(id)
                .map(tombolaMapper::toDto);
    }

    @Override
    public boolean delete(Long id) {
        if (!tombolaRepository.existsById(id)) {
            return false;
        }
        tombolaRepository.deleteById(id);
        return true;
    }


    /**
     * Fetch all players for a given tombola.
     * Throws 404 if tombola not found.
     */
    @Override
    public List<PlayerDto> getPlayers(Long tombolaId) {
        // 1) Verify tombola exists
        tombolaRepository.findById(tombolaId)
                .orElseThrow(() -> new EntityNotFoundException("Tombola not found: " + tombolaId));

        // 2) Delegate to PlayerService
        return playerService.getPlayersByTombola(tombolaId);
    }
}
