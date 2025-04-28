package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.LotDto;
import com.frequencies.tombola.service.LotService;
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

@WebMvcTest(LotController.class)
@AutoConfigureMockMvc(addFilters = false)  // Désactive les filtres de sécurité
public class LotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LotService lotService;

    @Test
    void testGetAllLots() throws Exception {
        LotDto lot1 = LotDto.builder().id(1L).name("Lot 1").description("Desc 1").build();
        LotDto lot2 = LotDto.builder().id(2L).name("Lot 2").description("Desc 2").build();

        when(lotService.getAllLots()).thenReturn(Arrays.asList(lot1, lot2));

        mockMvc.perform(get("/lots")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Lot 1"))
                .andExpect(jsonPath("$[1].description").value("Desc 2"));
    }

    @Test
    void testGetLotByIdFound() throws Exception {
        LotDto lot = LotDto.builder().id(1L).name("Lot Unique").description("Desc").build();

        when(lotService.getLotById(1L)).thenReturn(Optional.of(lot));

        mockMvc.perform(get("/lots/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lot Unique"));
    }

    @Test
    void testGetLotByIdNotFound() throws Exception {
        when(lotService.getLotById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/lots/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
