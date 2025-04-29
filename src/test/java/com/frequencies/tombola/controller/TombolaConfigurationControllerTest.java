package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.TombolaConfigurationDto;
import com.frequencies.tombola.service.TombolaConfigurationService;
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

@WebMvcTest(TombolaConfigurationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TombolaConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TombolaConfigurationService configService;

    @Test
    void testGetConfig() throws Exception {
        when(configService.getTicketsPerParticipant()).thenReturn(3);
        when(configService.isGuaranteeEnabled()).thenReturn(true);

        mockMvc.perform(get("/config")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketsPerParticipant").value(3))
                .andExpect(jsonPath("$.guaranteeOneLotPerParticipant").value(true));
    }
}
