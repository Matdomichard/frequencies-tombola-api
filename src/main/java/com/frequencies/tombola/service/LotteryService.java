package com.frequencies.tombola.service;

import com.frequencies.tombola.entity.Ticket;
import com.frequencies.tombola.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
public class LotteryService {

    private final TicketRepository ticketRepository;

    public LotteryService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Optional<Ticket> drawWinner() {
        List<Ticket> eligibleTickets = ticketRepository.findByIsWinnerFalse();
        if (eligibleTickets.isEmpty()) {
            return Optional.empty();
        }
        Ticket winner = eligibleTickets.get(new Random().nextInt(eligibleTickets.size()));
        winner.setWinner(true);
        ticketRepository.save(winner);
        return Optional.of(winner);
    }
}
