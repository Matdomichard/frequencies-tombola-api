package com.frequencies.tombola.service;

import com.frequencies.tombola.dto.TicketDto;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    List<TicketDto> getAllTickets();
    Optional<TicketDto> getTicketById(Long id);
    TicketDto createTicket(TicketDto ticketDto);
    List<TicketDto> getTicketsByPlayerId(Long playerId);
    Optional<TicketDto> markTicketAsWinner(Long id);
    Optional<TicketDto> claimPrize(Long id);
    List<TicketDto> getWinningTickets();

}
