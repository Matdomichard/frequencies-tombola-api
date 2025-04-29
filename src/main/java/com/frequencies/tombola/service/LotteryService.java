package com.frequencies.tombola.service;

import com.frequencies.tombola.dto.TicketDto;

import java.util.Optional;

public interface LotteryService {
    Optional<TicketDto> drawWinner();
}
