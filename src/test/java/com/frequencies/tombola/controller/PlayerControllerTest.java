package com.frequencies.tombola.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frequencies.tombola.config.NoSecurityConfig;
import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.enums.PaymentMethod;
import com.frequencies.tombola.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayerController.class)
@Import(NoSecurityConfig.class)
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @Autowired
    private ObjectMapper objectMapper;

    private PlayerDto player;

    @BeforeEach
    void setUp() {
        player = PlayerDto.builder()
                .id(42L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("0600000000")
                .ticketNumber(123)
                .paymentMethod(PaymentMethod.CARD)
                .build();
    }

    /**
     * Should return 200 OK and player data when player exists.
     */
    @Test
    void getPlayerById_found() throws Exception {
        Mockito.when(playerService.getPlayerById(42L)).thenReturn(Optional.of(player));

        mockMvc.perform(get("/players/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    /**
     * Should return 404 when player does not exist.
     */
    @Test
    void getPlayerById_notFound() throws Exception {
        Mockito.when(playerService.getPlayerById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/players/99"))
                .andExpect(status().isNotFound());
    }

    /**
     * Should return 200 OK and updated player on full update.
     */
    @Test
    void updatePlayer_success() throws Exception {
        PlayerDto updated = PlayerDto.builder()
                .id(42L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phone("0600000001")
                .ticketNumber(124)
                .paymentMethod(PaymentMethod.CASH)
                .build();

        Mockito.when(playerService.updatePlayer(eq(42L), any(PlayerDto.class)))
                .thenReturn(Optional.of(updated));

        mockMvc.perform(put("/players/42")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"));
    }

    /**
     * Should return 404 if trying to update non-existent player.
     */
    @Test
    void updatePlayer_notFound() throws Exception {
        Mockito.when(playerService.updatePlayer(eq(99L), any(PlayerDto.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/players/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isNotFound());
    }

    /**
     * Should return 200 OK and updated player on partial update (PATCH).
     */
    @Test
    void patchPlayer_success() throws Exception {
        Map<String, Object> changes = Map.of("firstName", "Patched");
        PlayerDto patched = PlayerDto.builder()
                .id(42L)
                .firstName("Patched")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("0600000000")
                .ticketNumber(123)
                .paymentMethod(PaymentMethod.CARD)
                .build();

        Mockito.when(playerService.patchPlayer(eq(42L), any(Map.class)))
                .thenReturn(Optional.of(patched));

        mockMvc.perform(patch("/players/42")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changes)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Patched"));
    }

    /**
     * Should return 404 on PATCH for non-existent player.
     */
    @Test
    void patchPlayer_notFound() throws Exception {
        Map<String, Object> changes = Map.of("firstName", "Patched");
        Mockito.when(playerService.patchPlayer(eq(99L), any(Map.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(patch("/players/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changes)))
                .andExpect(status().isNotFound());
    }

    /**
     * Should return 204 No Content when deleting an existing player.
     */
    @Test
    void deletePlayer_success() throws Exception {
        Mockito.when(playerService.deletePlayer(42L)).thenReturn(true);

        mockMvc.perform(delete("/players/42"))
                .andExpect(status().isNoContent());
    }

    /**
     * Should return 404 when deleting a non-existent player.
     */
    @Test
    void deletePlayer_notFound() throws Exception {
        Mockito.when(playerService.deletePlayer(99L)).thenReturn(false);

        mockMvc.perform(delete("/players/99"))
                .andExpect(status().isNotFound());
    }
}
