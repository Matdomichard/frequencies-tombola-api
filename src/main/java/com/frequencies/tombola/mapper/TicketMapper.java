package com.frequencies.tombola.mapper;

import com.frequencies.tombola.dto.TicketDto;
import com.frequencies.tombola.entity.Player;
import com.frequencies.tombola.entity.Ticket;

public class TicketMapper {

    public static TicketDto toDto(Ticket ticket) {
        return TicketDto.builder()
                .id(ticket.getId())
                .playerId(ticket.getPlayer().getId())
                .isWinner(ticket.isWinner())
                .hasClaimedPrize(ticket.isHasClaimedPrize())
                .build();
    }

    public static Ticket toEntity(TicketDto ticketDto, Player player) {
        return Ticket.builder()
                .id(ticketDto.getId())
                .player(player)
                .isWinner(ticketDto.isWinner())
                .hasClaimedPrize(ticketDto.isHasClaimedPrize())
                .build();
    }

}
