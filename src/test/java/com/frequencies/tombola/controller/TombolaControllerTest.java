package com.frequencies.tombola.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frequencies.tombola.dto.LotDto;
import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.dto.TombolaDto;
import com.frequencies.tombola.enums.PaymentMethod;
import com.frequencies.tombola.service.LotService;
import com.frequencies.tombola.service.PlayerService;
import com.frequencies.tombola.service.TombolaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(com.frequencies.tombola.config.NoSecurityConfig.class)
@WebMvcTest(TombolaController.class)
class TombolaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TombolaService tombolaService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private LotService lotService;

    @Autowired
    private ObjectMapper objectMapper;

    private TombolaDto tombola1, tombola2;
    private PlayerDto player1, player2;
    private LotDto lot1, lot2;

    @BeforeEach
    void setUp() {
        tombola1 = TombolaDto.builder()
                .id(1L)
                .name("Tombola 1")
                .helloAssoFormSlug("slug1")
                .createdAt(LocalDateTime.now())
                .build();
        tombola2 = TombolaDto.builder()
                .id(2L)
                .name("Tombola 2")
                .helloAssoFormSlug("slug2")
                .createdAt(LocalDateTime.now())
                .build();

        player1 = PlayerDto.builder()
                .id(1L)
                .firstName("Player")
                .lastName("One")
                .email("player1@example.com")
                .phone("0600000001")
                .ticketNumber(1)
                .paymentMethod(PaymentMethod.CARD)
                .build();
        player2 = PlayerDto.builder()
                .id(2L)
                .firstName("Player")
                .lastName("Two")
                .email("player2@example.com")
                .phone("0600000002")
                .ticketNumber(2)
                .paymentMethod(PaymentMethod.CASH)
                .build();

        lot1 = LotDto.builder()
                .id(1L)
                .name("Lot 1")
                .tombolaId(1L)
                .value(new BigDecimal("10.00"))
                .build();
        lot2 = LotDto.builder()
                .id(2L)
                .name("Lot 2")
                .tombolaId(1L)
                .value(new BigDecimal("15.00"))
                .build();
    }

    @Test
    void getAllTombolas_returnsList() throws Exception {
        Mockito.when(tombolaService.getAll()).thenReturn(Arrays.asList(tombola1, tombola2));

        mockMvc.perform(get("/tombolas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Tombola 1"));
    }

    @Test
    void getById_found() throws Exception {
        Mockito.when(tombolaService.getById(1L)).thenReturn(Optional.of(tombola1));

        mockMvc.perform(get("/tombolas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tombola 1"));
    }

    @Test
    void getById_notFound() throws Exception {
        Mockito.when(tombolaService.getById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tombolas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTombola_success() throws Exception {
        TombolaDto request = TombolaDto.builder()
                .name("Nouvelle tombola")
                .helloAssoFormSlug("slug-nouvelle")
                .build();
        TombolaDto saved = TombolaDto.builder()
                .id(123L)
                .name("Nouvelle tombola")
                .helloAssoFormSlug("slug-nouvelle")
                .build();

        Mockito.when(tombolaService.create(any(TombolaDto.class))).thenReturn(saved);

        mockMvc.perform(post("/tombolas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(123L));
    }

    @Test
    void deleteTombola_found() throws Exception {
        Mockito.when(tombolaService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/tombolas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTombola_notFound() throws Exception {
        Mockito.when(tombolaService.delete(999L)).thenReturn(false);

        mockMvc.perform(delete("/tombolas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPlayers_returnsList() throws Exception {
        Mockito.when(playerService.getPlayersByTombola(1L)).thenReturn(Arrays.asList(player1, player2));

        mockMvc.perform(get("/tombolas/1/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("Player"))
                .andExpect(jsonPath("$[0].lastName").value("One"))
                .andExpect(jsonPath("$[1].lastName").value("Two"));
    }

    @Test
    void createPlayer_success() throws Exception {
        PlayerDto req = PlayerDto.builder()
                .firstName("New")
                .lastName("Player")
                .email("newplayer@example.com")
                .phone("0600000011")
                .ticketNumber(11)
                .paymentMethod(PaymentMethod.CARD)
                .build();

        PlayerDto saved = PlayerDto.builder()
                .id(22L)
                .firstName("New")
                .lastName("Player")
                .email("newplayer@example.com")
                .phone("0600000011")
                .ticketNumber(11)
                .paymentMethod(PaymentMethod.CARD)
                .build();

        Mockito.when(playerService.createPlayer(eq(1L), any(PlayerDto.class))).thenReturn(saved);

        mockMvc.perform(post("/tombolas/1/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(22L))
                .andExpect(jsonPath("$.firstName").value("New"));
    }

    @Test
    void getLots_returnsList() throws Exception {
        Mockito.when(lotService.getLotsByTombola(1L)).thenReturn(Arrays.asList(lot1, lot2));

        mockMvc.perform(get("/tombolas/1/lots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Lot 1"))
                .andExpect(jsonPath("$[1].name").value("Lot 2"));
    }

    @Test
    void createLot_success() throws Exception {
        LotDto req = LotDto.builder()
                .name("Lot Super")
                .tombolaId(1L)
                .value(new BigDecimal("20.00"))
                .build();

        LotDto saved = LotDto.builder()
                .id(222L)
                .name("Lot Super")
                .tombolaId(1L)
                .value(new BigDecimal("20.00"))
                .build();

        Mockito.when(lotService.createLot(eq(1L), any(LotDto.class))).thenReturn(saved);

        mockMvc.perform(post("/tombolas/1/lots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(222L));
    }
}
