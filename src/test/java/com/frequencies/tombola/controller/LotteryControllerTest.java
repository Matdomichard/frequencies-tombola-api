package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.TicketDto;
import com.frequencies.tombola.service.LotteryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LotteryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LotteryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LotteryService lotteryService;

    @Test
    void testDrawWinnerSuccess() throws Exception {
        TicketDto ticketDto = TicketDto.builder()
                .id(1L)
                .playerId(1L)
                .isWinner(true)
                .build();

        when(lotteryService.drawWinner()).thenReturn(Optional.of(ticketDto));

        mockMvc.perform(post("/lottery/draw")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.winner").value(true));
    }

    @Test
    void testDrawWinnerNoTickets() throws Exception {
        when(lotteryService.drawWinner()).thenReturn(Optional.empty());

        mockMvc.perform(post("/lottery/draw")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
