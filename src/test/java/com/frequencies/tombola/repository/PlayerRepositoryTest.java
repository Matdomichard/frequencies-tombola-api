package com.frequencies.tombola.repository;

import com.frequencies.tombola.entity.Player;
import com.frequencies.tombola.entity.Tombola;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TombolaRepository tombolaRepository;

    private Tombola tombola;

    @BeforeEach
    void setUp() {
        // Create a tombola for the players
        tombola = new Tombola();
        tombola.setName("Test Tombola");
        tombola = tombolaRepository.save(tombola);
    }

    @Test
    void testSavePlayer() {
        // Given
        Player player = Player.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice@mail.com")
                .phoneNumber("1234567890")
                .tombola(tombola)
                .build();

        // When
        Player savedPlayer = playerRepository.save(player);

        // Then
        assertThat(savedPlayer.getId()).isNotNull();
        assertThat(savedPlayer.getFirstName()).isEqualTo("Alice");
        assertThat(savedPlayer.getLastName()).isEqualTo("Smith");
    }

    @Test
    void testFindById() {
        // Given
        Player player = Player.builder()
                .firstName("Bob")
                .lastName("Johnson")
                .email("bob@mail.com")
                .phoneNumber("0987654321")
                .tombola(tombola)
                .build();
        player = playerRepository.save(player);

        // When
        Optional<Player> foundPlayer = playerRepository.findById(player.getId());

        // Then
        assertThat(foundPlayer).isPresent();
        assertThat(foundPlayer.get().getEmail()).isEqualTo("bob@mail.com");
    }

    @Test
    void testFindByTombolaId() {
        // Given
        Player player1 = Player.builder()
                .firstName("Charlie")
                .lastName("Brown")
                .email("charlie@mail.com")
                .phoneNumber("111222333")
                .tombola(tombola)
                .build();
        playerRepository.save(player1);

        Player player2 = Player.builder()
                .firstName("David")
                .lastName("Miller")
                .email("david@mail.com")
                .phoneNumber("444555666")
                .tombola(tombola)
                .build();
        playerRepository.save(player2);

        // When
        List<Player> players = playerRepository.findByTombolaId(tombola.getId());

        // Then
        assertThat(players).hasSize(2);
        assertThat(players.stream().map(Player::getEmail))
                .contains("charlie@mail.com", "david@mail.com");
    }

    @Test
    void testDeletePlayer() {
        // Given
        Player player = Player.builder()
                .firstName("Eve")
                .lastName("Wilson")
                .email("eve@mail.com")
                .phoneNumber("777888999")
                .tombola(tombola)
                .build();
        player = playerRepository.save(player);
        Long id = player.getId();

        // When
        playerRepository.deleteById(id);

        // Then
        Optional<Player> deletedPlayer = playerRepository.findById(id);
        assertThat(deletedPlayer).isNotPresent();
    }
}