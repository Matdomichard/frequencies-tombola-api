package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.StatsDto;
import com.frequencies.tombola.service.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatsService statsService;

    @Test
    void testGetStats() throws Exception {
        StatsDto stats = new StatsDto(100, 200, 50, 30);

        when(statsService.getStats()).thenReturn(stats);

        mockMvc.perform(get("/stats").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPlayers").value(100))
                .andExpect(jsonPath("$.totalTickets").value(200))
                .andExpect(jsonPath("$.totalWinners").value(50))
                .andExpect(jsonPath("$.remainingLots").value(30));
    }
}
