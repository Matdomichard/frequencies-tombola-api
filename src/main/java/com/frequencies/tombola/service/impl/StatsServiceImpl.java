package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.StatsDto;
import com.frequencies.tombola.repository.LotRepository;
import com.frequencies.tombola.repository.PlayerRepository;
import com.frequencies.tombola.repository.TicketRepository;
import com.frequencies.tombola.service.StatsService;
import org.springframework.stereotype.Service;

@Service
public class StatsServiceImpl implements StatsService {

    private final PlayerRepository playerRepository;
    private final TicketRepository ticketRepository;
    private final LotRepository lotRepository;

    public StatsServiceImpl(PlayerRepository playerRepository, TicketRepository ticketRepository, LotRepository lotRepository) {
        this.playerRepository = playerRepository;
        this.ticketRepository = ticketRepository;
        this.lotRepository = lotRepository;
    }

    @Override
    public StatsDto getStats() {
        long totalPlayers = playerRepository.count();
        long totalTickets = ticketRepository.count();
        long totalWinners = ticketRepository.findByIsWinnerTrue().size();
        long remainingLots = lotRepository.count();

        return new StatsDto(totalPlayers, totalTickets, totalWinners, remainingLots);
    }
}
