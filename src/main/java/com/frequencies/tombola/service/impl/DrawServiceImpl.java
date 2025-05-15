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
        // Get all available lots and players for this tombola
        List<Lot> availableLots = lotRepo.findByTombola_Id(tombolaId)
                .stream()
                .filter(lot -> lot.getStatus() == LotStatus.UNASSIGNED)
                .collect(Collectors.toList());
        List<Player> players = playerRepo.findByTombolaId(tombolaId);

        if (availableLots.isEmpty() || players.isEmpty()) {
            return new DrawResultDto(Collections.emptyList(), Collections.emptyList());
        }

        List<Lot> assignedLots = new ArrayList<>();

        // Build the ticket pool: each player appears as many times as their ticket count
        List<Player> ticketPool = new ArrayList<>();
        for (Player player : players) {
            for (int i = 0; i < player.getTicketNumber(); i++) {
                ticketPool.add(player);
            }
        }

        if (guaranteeOneLotPerParticipant) {
            // Step 1: Guarantee at least one lot per participant if possible
            Set<Player> playersWithLot = new HashSet<>();
            List<Lot> stepOneLots = new ArrayList<>();

            List<Lot> lotsCopy = new ArrayList<>(availableLots);

            while (playersWithLot.size() < players.size() && !lotsCopy.isEmpty()) {
                // Only players without a lot
                List<Player> remainingTicketPool = new ArrayList<>();
                for (Player player : players) {
                    if (!playersWithLot.contains(player)) {
                        for (int i = 0; i < player.getTicketNumber(); i++) {
                            remainingTicketPool.add(player);
                        }
                    }
                }
                if (remainingTicketPool.isEmpty()) {
                    break;
                }
                int randomIndex = ThreadLocalRandom.current().nextInt(remainingTicketPool.size());
                Player winner = remainingTicketPool.get(randomIndex);

                Lot lot = lotsCopy.removeFirst();
                lot.setAssignedTo(winner);
                lot.setStatus(LotStatus.ASSIGNED);
                assignedLots.add(lot);
                stepOneLots.add(lot);
                playersWithLot.add(winner);

                // Remove just one instance of this winner from the original ticket pool
                ticketPool.remove(winner);
            }

            // Remove assigned lots from availableLots (keep only the remaining)
            availableLots.removeAll(stepOneLots);
        }

        // Step 2: Assign all remaining lots randomly (weighted by remaining tickets)
        // (Here, ticketPool contains all tickets that haven't been used for step 1)
        while (!availableLots.isEmpty() && !ticketPool.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(ticketPool.size());
            Player winner = ticketPool.get(randomIndex);

            Lot lot = availableLots.removeFirst();
            lot.setAssignedTo(winner);
            lot.setStatus(LotStatus.ASSIGNED);
            assignedLots.add(lot);

            // Remove just one instance of winner (to use a ticket)
            ticketPool.remove(randomIndex);
        }

        // If lots remain (e.g., less tickets than lots), assign them purely randomly among all players
        while (!availableLots.isEmpty()) {
            int randomIndex = random.nextInt(players.size());
            Player winner = players.get(randomIndex);

            Lot lot = availableLots.removeFirst();
            lot.setAssignedTo(winner);
            lot.setStatus(LotStatus.ASSIGNED);
            assignedLots.add(lot);
        }

        // Save all assigned lots
        lotRepo.saveAll(assignedLots);

        // Convert to DTOs
        List<LotDto> lotDtos = assignedLots.stream()
                .map(lotMapper::toDto)
                .collect(Collectors.toList());

        // Return result with empty players list (since we don't have PlayerDto conversion here)
        return new DrawResultDto(Collections.emptyList(), lotDtos);
    }
}
