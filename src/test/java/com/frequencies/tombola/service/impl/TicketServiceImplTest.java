package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.entity.TombolaConfiguration;
import com.frequencies.tombola.repository.TombolaConfigurationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TombolaConfigurationServiceImplTest {

    @Mock
    private TombolaConfigurationRepository repository;

    @InjectMocks
    private TombolaConfigurationServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSetTicketsPerParticipant() {
        TombolaConfiguration config = new TombolaConfiguration();
        when(repository.findAll()).thenReturn(Collections.singletonList(config));
        when(repository.save(any())).thenReturn(config);

        service.setTicketsPerParticipant(5);

        verify(repository, times(1)).save(config);
        assertThat(config.getTicketsPerParticipant()).isEqualTo(5);
    }

    @Test
    void testGetTicketsPerParticipant() {
        TombolaConfiguration config = new TombolaConfiguration();
        config.setTicketsPerParticipant(3);
        when(repository.findAll()).thenReturn(Collections.singletonList(config));

        int tickets = service.getTicketsPerParticipant();

        assertThat(tickets).isEqualTo(3);
    }

    @Test
    void testEnableGuaranteeOneLotPerParticipant() {
        TombolaConfiguration config = new TombolaConfiguration();
        when(repository.findAll()).thenReturn(Collections.singletonList(config));
        when(repository.save(any())).thenReturn(config);

        service.enableGuaranteeOneLotPerParticipant();

        verify(repository, times(1)).save(config);
        assertThat(config.getGuaranteeOneLotPerParticipant()).isTrue();
    }

    @Test
    void testDisableGuaranteeOneLotPerParticipant() {
        TombolaConfiguration config = new TombolaConfiguration();
        config.setGuaranteeOneLotPerParticipant(true);
        when(repository.findAll()).thenReturn(Collections.singletonList(config));
        when(repository.save(any())).thenReturn(config);

        service.disableGuaranteeOneLotPerParticipant();

        verify(repository, times(1)).save(config);
        assertThat(config.getGuaranteeOneLotPerParticipant()).isFalse();
    }

    @Test
    void testIsGuaranteeEnabled() {
        TombolaConfiguration config = new TombolaConfiguration();
        config.setGuaranteeOneLotPerParticipant(true);
        when(repository.findAll()).thenReturn(Collections.singletonList(config));

        boolean guaranteeEnabled = service.isGuaranteeEnabled();

        assertThat(guaranteeEnabled).isTrue();
    }
}
