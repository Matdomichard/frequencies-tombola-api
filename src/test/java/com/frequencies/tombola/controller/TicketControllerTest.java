package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.TicketDto;
import com.frequencies.tombola.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Test
    void testGetAllTickets() throws Exception {
        TicketDto ticket = TicketDto.builder().id(1L).playerId(1L).isWinner(false).build();
        when(ticketService.getAllTickets()).thenReturn(List.of(ticket));

        mockMvc.perform(get("/tickets")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetTicketByIdFound() throws Exception {
        TicketDto ticket = TicketDto.builder().id(1L).playerId(1L).isWinner(false).build();
        when(ticketService.getTicketById(1L)).thenReturn(Optional.of(ticket));

        mockMvc.perform(get("/tickets/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetTicketByIdNotFound() throws Exception {
        when(ticketService.getTicketById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tickets/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetWinningTickets() throws Exception {
        TicketDto ticket1 = TicketDto.builder().id(1L).playerId(1L).isWinner(true).build();
        TicketDto ticket2 = TicketDto.builder().id(2L).playerId(2L).isWinner(true).build();

        when(ticketService.getWinningTickets()).thenReturn(List.of(ticket1, ticket2));

        mockMvc.perform(get("/tickets/winners")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].winner").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].winner").value(true));
    }

}
