package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.TicketDto;
import com.frequencies.tombola.entity.Ticket;
import com.frequencies.tombola.mapper.TicketMapper;
import com.frequencies.tombola.repository.TicketRepository;
import com.frequencies.tombola.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class LotteryServiceImpl implements LotteryService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Optional<TicketDto> drawWinner() {
        List<Ticket> eligibleTickets = ticketRepository.findByIsWinnerFalse();
        if (eligibleTickets.isEmpty()) {
            return Optional.empty();
        }

        Ticket winnerTicket = eligibleTickets.get(new Random().nextInt(eligibleTickets.size()));
        winnerTicket.setWinner(true);
        ticketRepository.save(winnerTicket);

        return Optional.of(TicketMapper.toDto(winnerTicket));
    }
}
