package com.frequencies.tombola.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsDto {
    private long totalPlayers;
    private long totalTickets;
    private long totalWinners;
    private long remainingLots;
}
