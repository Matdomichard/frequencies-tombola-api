package com.frequencies.tombola.controller;

import com.frequencies.tombola.service.TombolaConfigurationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TombolaConfigurationController.class)
public class TombolaConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TombolaConfigurationService configurationService;

    @Test
    void testGetTicketsPerParticipant() throws Exception {
        when(configurationService.getTicketsPerParticipant()).thenReturn(5);

        mockMvc.perform(get("/admin/configuration/tickets-per-participant")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void testSetTicketsPerParticipant() throws Exception {
        doNothing().when(configurationService).setTicketsPerParticipant(3);

        mockMvc.perform(post("/admin/configuration/tickets-per-participant")
                        .param("count", "3"))
                .andExpect(status().isOk());
    }

    @Test
    void testIsGuaranteeEnabled() throws Exception {
        when(configurationService.isGuaranteeEnabled()).thenReturn(true);

        mockMvc.perform(get("/admin/configuration/guarantee")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testEnableGuarantee() throws Exception {
        doNothing().when(configurationService).enableGuaranteeOneLotPerParticipant();

        mockMvc.perform(post("/admin/configuration/guarantee/enable"))
                .andExpect(status().isOk());
    }

    @Test
    void testDisableGuarantee() throws Exception {
        doNothing().when(configurationService).disableGuaranteeOneLotPerParticipant();

        mockMvc.perform(post("/admin/configuration/guarantee/disable"))
                .andExpect(status().isOk());
    }
}
