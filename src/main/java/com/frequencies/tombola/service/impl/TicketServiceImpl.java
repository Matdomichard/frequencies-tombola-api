package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.TicketDto;
import com.frequencies.tombola.entity.Player;
import com.frequencies.tombola.entity.Ticket;
import com.frequencies.tombola.mapper.TicketMapper;
import com.frequencies.tombola.repository.PlayerRepository;
import com.frequencies.tombola.repository.TicketRepository;
import com.frequencies.tombola.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public List<TicketDto> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(TicketMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TicketDto> getTicketById(Long id) {
        return ticketRepository.findById(id)
                .map(TicketMapper::toDto);
    }

    @Override
    public TicketDto createTicket(TicketDto ticketDto) {
        Player player = playerRepository.findById(ticketDto.getPlayerId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        Ticket ticket = Ticket.builder()
                .player(player)
                .isWinner(false)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);
        return TicketMapper.toDto(savedTicket);
    }

    @Override
    public List<TicketDto> getTicketsByPlayerId(Long playerId) {
        List<Ticket> tickets = ticketRepository.findByPlayerId(playerId);
        return tickets.stream().map(TicketMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<TicketDto> markTicketAsWinner(Long id) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setWinner(true);
            return TicketMapper.toDto(ticketRepository.save(ticket));
        });
    }

    @Override
    public Optional<TicketDto> claimPrize(Long id) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setHasClaimedPrize(true);
                    Ticket savedTicket = ticketRepository.save(ticket);
                    return TicketMapper.toDto(savedTicket);
                });
    }

    @Override
    public List<TicketDto> getWinningTickets() {
        return ticketRepository.findByIsWinnerTrue()
                .stream()
                .map(TicketMapper::toDto)
                .toList();
    }


}
