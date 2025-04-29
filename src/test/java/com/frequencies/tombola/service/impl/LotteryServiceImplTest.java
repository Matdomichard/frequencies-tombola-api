package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.TicketDto;
import com.frequencies.tombola.entity.Player;
import com.frequencies.tombola.entity.Ticket;
import com.frequencies.tombola.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LotteryServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private LotteryServiceImpl lotteryService;

    @Test
    void testDrawWinnerWhenTicketsAvailable() {
        // Given
        Player player = Player.builder().id(1L).name("Alice").build();
        Ticket ticket1 = Ticket.builder().id(1L).player(player).isWinner(false).build();
        Ticket ticket2 = Ticket.builder().id(2L).player(player).isWinner(false).build();

        when(ticketRepository.findByIsWinnerFalse()).thenReturn(List.of(ticket1, ticket2));
        when(ticketRepository.save(ticket1)).thenReturn(ticket1);  // Peut aussi mocker save pour ticket2 si besoin

        // When
        Optional<TicketDto> winner = lotteryService.drawWinner();

        // Then
        assertThat(winner).isPresent();
        assertThat(winner.get().isWinner()).isTrue();
    }

    @Test
    void testDrawWinnerWhenNoTicketsAvailable() {
        // Given
        when(ticketRepository.findByIsWinnerFalse()).thenReturn(List.of());

        // When
        Optional<TicketDto> winner = lotteryService.drawWinner();

        // Then
        assertThat(winner).isNotPresent();
    }
}
