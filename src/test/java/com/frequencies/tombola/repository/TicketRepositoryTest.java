package com.frequencies.tombola.repository;

import com.frequencies.tombola.entity.Player;
import com.frequencies.tombola.entity.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void testSaveTicket() {
        Player player = playerRepository.save(Player.builder()
                .name("Test Player")
                .email("test@player.com")
                .phoneNumber("123456789")
                .build());

        Ticket ticket = Ticket.builder()
                .player(player)
                .isWinner(false)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        assertThat(savedTicket.getId()).isNotNull();
        assertThat(savedTicket.getPlayer().getId()).isEqualTo(player.getId());
    }

    @Test
    void testFindByPlayerId() {
        Player player = playerRepository.save(Player.builder()
                .name("Player A")
                .email("a@player.com")
                .phoneNumber("0000000")
                .build());

        Ticket ticket = ticketRepository.save(Ticket.builder()
                .player(player)
                .isWinner(false)
                .build());

        List<Ticket> tickets = ticketRepository.findByPlayerId(player.getId());

        assertThat(tickets).hasSize(1);
        assertThat(tickets.getFirst().getPlayer().getId()).isEqualTo(player.getId());
    }
}
