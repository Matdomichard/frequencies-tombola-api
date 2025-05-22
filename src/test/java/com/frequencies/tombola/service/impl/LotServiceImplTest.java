package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.LotDto;
import com.frequencies.tombola.entity.Lot;
import com.frequencies.tombola.mapper.LotMapper;
import com.frequencies.tombola.repository.LotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LotServiceImplTest {

    @Mock
    private LotRepository lotRepository;

    @Mock
    private LotMapper lotMapper;

    @InjectMocks
    private LotServiceImpl lotService;

    private Lot lot1;
    private Lot lot2;
    private LotDto lotDto1;
    private LotDto lotDto2;

    @BeforeEach
    void setUp() {
        lot1 = Lot.builder()
                .id(1L)
                .name("Lot 1")
                .build();

        lot2 = Lot.builder()
                .id(2L)
                .name("Lot 2")
                .build();

        lotDto1 = LotDto.builder()
                .id(1L)
                .name("Lot 1")
                .build();

        lotDto2 = LotDto.builder()
                .id(2L)
                .name("Lot 2")
                .build();
    }

    @Test
    void testGetAllLots() {
        // given
        List<Lot> lots = Arrays.asList(lot1, lot2);
        when(lotRepository.findAll()).thenReturn(lots);
        when(lotMapper.toDto(lot1)).thenReturn(lotDto1);
        when(lotMapper.toDto(lot2)).thenReturn(lotDto2);

        // when
        List<LotDto> result = lotService.getAllLots();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Lot 1");
        assertThat(result.get(1).getName()).isEqualTo("Lot 2");
    }

    @Test
    void testGetLotByIdFound() {
        // given
        when(lotRepository.findById(1L)).thenReturn(Optional.of(lot1));
        when(lotMapper.toDto(lot1)).thenReturn(lotDto1);

        // when
        Optional<LotDto> result = lotService.getLotById(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Lot 1");
    }

    @Test
    void testGetLotByIdNotFound() {
        // given
        when(lotRepository.findById(3L)).thenReturn(Optional.empty());

        // when
        Optional<LotDto> result = lotService.getLotById(3L);

        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testCreateLot() {
        // given
        LotDto newLotDto = LotDto.builder()
                .name("New Lot")
                .build();

        Lot newLot = Lot.builder()
                .name("New Lot")
                .build();

        Lot savedLot = Lot.builder()
                .id(3L)
                .name("New Lot")
                .build();

        LotDto savedLotDto = LotDto.builder()
                .id(3L)
                .name("New Lot")
                .build();

        when(lotMapper.toEntity(newLotDto)).thenReturn(newLot);
        when(lotRepository.save(any(Lot.class))).thenReturn(savedLot);
        when(lotMapper.toDto(savedLot)).thenReturn(savedLotDto);

        // when
        LotDto result = lotService.createLot(newLotDto);

        // then
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("New Lot");
    }

    @Test
    void testUpdateLotFound() {
        // given
        LotDto updatedLotDto = LotDto.builder()
                .name("Updated Lot")
                .build();

        Lot updatedLot = Lot.builder()
                .id(1L)
                .name("Updated Lot")
                .build();

        LotDto resultLotDto = LotDto.builder()
                .id(1L)
                .name("Updated Lot")
                .build();

        when(lotRepository.findById(1L)).thenReturn(Optional.of(lot1));
        when(lotRepository.save(any(Lot.class))).thenReturn(updatedLot);
        when(lotMapper.toDto(updatedLot)).thenReturn(resultLotDto);

        // when
        Optional<LotDto> result = lotService.updateLot(1L, updatedLotDto);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Updated Lot");
    }

    @Test
    void testUpdateLotNotFound() {
        // given
        LotDto updatedLotDto = LotDto.builder()
                .name("Updated Lot")
                .build();

        when(lotRepository.findById(3L)).thenReturn(Optional.empty());

        // when
        Optional<LotDto> result = lotService.updateLot(3L, updatedLotDto);

        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testDeleteLotFound() {
        // given
        when(lotRepository.existsById(1L)).thenReturn(true);
        doNothing().when(lotRepository).deleteById(1L);

        // when
        boolean result = lotService.deleteLot(1L);

        // then
        assertThat(result).isTrue();
        verify(lotRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteLotNotFound() {
        // given
        when(lotRepository.existsById(3L)).thenReturn(false);

        // when
        boolean result = lotService.deleteLot(3L);

        // then
        assertThat(result).isFalse();
        verify(lotRepository, never()).deleteById(anyLong());
    }
}