package com.frequencies.tombola.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketDto {
    private Long id;
    private Long playerId;
    private boolean isWinner;
    private boolean hasClaimedPrize;
}
