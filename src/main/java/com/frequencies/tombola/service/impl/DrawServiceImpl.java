package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.DrawResultDto;
import com.frequencies.tombola.dto.LotDto;
import com.frequencies.tombola.entity.Lot;
import com.frequencies.tombola.entity.LotStatus;
import com.frequencies.tombola.entity.Player;
import com.frequencies.tombola.mapper.LotMapper;
import com.frequencies.tombola.repository.LotRepository;
import com.frequencies.tombola.repository.PlayerRepository;
import com.frequencies.tombola.service.DrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrawServiceImpl implements DrawService {

    private final PlayerRepository playerRepo;
    private final LotRepository lotRepo;
    private final LotMapper lotMapper;
    private final Random random = new Random();

    @Override
    @Transactional
    public DrawResultDto draw(Long tombolaId, boolean guaranteeOneLotPerParticipant) {
        // Get all available lots and players
        List<Lot> availableLots = lotRepo.findByTombola_Id(tombolaId);
        List<Player> players = playerRepo.findByTombolaId(tombolaId);

        if (availableLots.isEmpty() || players.isEmpty()) {
            return new DrawResultDto(Collections.emptyList(), Collections.emptyList());
        }

        List<Lot> assignedLots = new ArrayList<>();

        // Créer une liste de tickets pour la sélection aléatoire pondérée
        List<Player> ticketPool = new ArrayList<>();
        for (Player player : players) {
            // Ajouter le joueur autant de fois qu'il a de tickets
            for (int i = 0; i < player.getTicketNumber(); i++) {
                ticketPool.add(player);
            }
        }

        if (guaranteeOneLotPerParticipant) {
            // First ensure each player gets at least one lot
            Set<Player> playersWithLot = new HashSet<>();

            // Keep drawing until each player has at least one lot or no more lots available
            while (playersWithLot.size() < players.size() && !availableLots.isEmpty()) {
                // Create pool only with players who don't have a lot yet
                List<Player> remainingTicketPool = new ArrayList<>();
                for (Player player : players) {
                    if (!playersWithLot.contains(player)) {
                        for (int i = 0; i < player.getTicketNumber(); i++) {
                            remainingTicketPool.add(player);
                        }
                    }
                }

                if (!remainingTicketPool.isEmpty()) {
                    // Select random player from remaining pool
                    int randomIndex = ThreadLocalRandom.current().nextInt(remainingTicketPool.size());
                    Player winner = remainingTicketPool.get(randomIndex);

                    // Assign lot
                    Lot lot = availableLots.remove(0);
                    lot.setAssignedTo(winner);
                    lot.setStatus(LotStatus.ASSIGNED);
                    assignedLots.add(lot);
                    playersWithLot.add(winner);
                }
            }
        }

        // Distribute remaining lots using full weighted random based on tickets
        while (!availableLots.isEmpty() && !ticketPool.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(ticketPool.size());
            Player winner = ticketPool.get(randomIndex);

            // Remove all instances of this player from the ticket pool to prevent bias
            ticketPool.removeIf(player -> player.getId().equals(winner.getId()));

            Lot lot = availableLots.remove(0);
            lot.setAssignedTo(winner);
            lot.setStatus(LotStatus.ASSIGNED);
            assignedLots.add(lot);
        }

        // Save all assigned lots
        lotRepo.saveAll(assignedLots);

        // Get all winners
        Set<Player> winners = assignedLots.stream()
                .map(Lot::getAssignedTo)
                .collect(Collectors.toSet());

        // Save players (no need to set winner status as there's no such field)
        playerRepo.saveAll(winners);

        // Convert to DTOs
        List<LotDto> lotDtos = assignedLots.stream()
                .map(lotMapper::toDto)
                .collect(Collectors.toList());

        // Return result with empty players list (since we don't have PlayerDto conversion here)
        return new DrawResultDto(Collections.emptyList(), lotDtos);
    }
}
