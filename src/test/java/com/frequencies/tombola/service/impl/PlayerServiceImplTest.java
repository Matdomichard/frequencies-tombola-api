package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.entity.Player;
import com.frequencies.tombola.mapper.PlayerMapper;
import com.frequencies.tombola.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Test
    void testGetAllPlayers() {
        List<Player> players = Arrays.asList(
                Player.builder().id(1L).name("Alice").email("alice@mail.com").build(),
                Player.builder().id(2L).name("Bob").email("bob@mail.com").build()
        );

        when(playerRepository.findAll()).thenReturn(players);

        List<PlayerDto> result = playerService.getAllPlayers();

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getName()).isEqualTo("Alice");
    }

    @Test
    void testGetPlayerByIdFound() {
        Player player = Player.builder().id(1L).name("Alice").email("alice@mail.com").build();

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        Optional<PlayerDto> result = playerService.getPlayerById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("alice@mail.com");
    }

    @Test
    void testGetPlayerByIdNotFound() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<PlayerDto> result = playerService.getPlayerById(99L);

        assertThat(result).isNotPresent();
    }

    @Test
    void testCreatePlayer() {
        PlayerDto dto = PlayerDto.builder().name("Charlie").email("charlie@mail.com").build();
        Player savedPlayer = PlayerMapper.toEntity(dto);
        savedPlayer.setId(3L);

        when(playerRepository.save(any(Player.class))).thenReturn(savedPlayer);

        PlayerDto result = playerService.createPlayer(dto);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Charlie");
    }

    @Test
    void testUpdatePlayerFound() {
        Player existing = Player.builder().id(1L).name("OldName").email("old@mail.com").build();
        PlayerDto updateDto = PlayerDto.builder().name("NewName").email("new@mail.com").build();

        when(playerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(playerRepository.save(any(Player.class))).thenReturn(existing);

        Optional<PlayerDto> result = playerService.updatePlayer(1L, updateDto);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("NewName");
    }

    @Test
    void testUpdatePlayerNotFound() {
        PlayerDto updateDto = PlayerDto.builder().name("NewName").email("new@mail.com").build();

        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<PlayerDto> result = playerService.updatePlayer(1L, updateDto);

        assertThat(result).isNotPresent();
    }

    @Test
    void testDeletePlayer() {
        when(playerRepository.existsById(1L)).thenReturn(true);

        boolean deleted = playerService.deletePlayer(1L);

        assertThat(deleted).isTrue();
        verify(playerRepository).deleteById(1L);
    }

    @Test
    void testDeletePlayerNotFound() {
        when(playerRepository.existsById(99L)).thenReturn(false);

        boolean deleted = playerService.deletePlayer(99L);

        assertThat(deleted).isFalse();
    }
}
