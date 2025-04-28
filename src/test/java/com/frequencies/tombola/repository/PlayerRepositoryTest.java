package com.frequencies.tombola.repository;

import com.frequencies.tombola.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void testSavePlayer() {
        // Given
        Player player = Player.builder()
                .name("Alice")
                .email("alice@mail.com")
                .phoneNumber("1234567890")
                .build();

        // When
        Player savedPlayer = playerRepository.save(player);

        // Then
        assertThat(savedPlayer.getId()).isNotNull();
        assertThat(savedPlayer.getName()).isEqualTo("Alice");
    }

    @Test
    void testFindById() {
        // Given
        Player player = Player.builder()
                .name("Bob")
                .email("bob@mail.com")
                .phoneNumber("0987654321")
                .build();
        player = playerRepository.save(player);

        // When
        Optional<Player> foundPlayer = playerRepository.findById(player.getId());

        // Then
        assertThat(foundPlayer).isPresent();
        assertThat(foundPlayer.get().getEmail()).isEqualTo("bob@mail.com");
    }

    @Test
    void testExistsByEmail() {
        // Given
        Player player = Player.builder()
                .name("Charlie")
                .email("charlie@mail.com")
                .phoneNumber("111222333")
                .build();
        playerRepository.save(player);

        // When
        boolean exists = playerRepository.existsByEmail("charlie@mail.com");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void testDeletePlayer() {
        // Given
        Player player = Player.builder()
                .name("David")
                .email("david@mail.com")
                .phoneNumber("444555666")
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
