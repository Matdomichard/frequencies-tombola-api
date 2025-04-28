package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @Test
    void testGetAllPlayers() throws Exception {
        PlayerDto player1 = PlayerDto.builder().id(1L).name("Alice").build();
        PlayerDto player2 = PlayerDto.builder().id(2L).name("Bob").build();

        when(playerService.getAllPlayers()).thenReturn(Arrays.asList(player1, player2));

        mockMvc.perform(get("/players").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    void testGetPlayerByIdFound() throws Exception {
        PlayerDto player = PlayerDto.builder().id(1L).name("Alice").build();

        when(playerService.getPlayerById(1L)).thenReturn(Optional.of(player));

        mockMvc.perform(get("/players/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void testGetPlayerByIdNotFound() throws Exception {
        when(playerService.getPlayerById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/players/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
