package com.frequencies.tombola.repository;

import com.frequencies.tombola.entity.Lot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LotRepositoryTest {

    @Autowired
    private LotRepository lotRepository;

    @Test
    void testSaveLot() {
        // Given
        Lot lot = Lot.builder()
                .name("Lot Test")
                .description("Description Test")
                .build();

        // When
        Lot savedLot = lotRepository.save(lot);

        // Then
        assertThat(savedLot.getId()).isNotNull();
        assertThat(savedLot.getName()).isEqualTo("Lot Test");
    }

    @Test
    void testFindById() {
        // Given
        Lot lot = Lot.builder()
                .name("Lot Find")
                .description("Find Desc")
                .build();
        lot = lotRepository.save(lot);

        // When
        Optional<Lot> foundLot = lotRepository.findById(lot.getId());

        // Then
        assertThat(foundLot).isPresent();
        assertThat(foundLot.get().getName()).isEqualTo("Lot Find");
    }

    @Test
    void testFindAll() {
        // Given
        Lot lot1 = Lot.builder().name("Lot 1").description("Desc 1").build();
        Lot lot2 = Lot.builder().name("Lot 2").description("Desc 2").build();
        lotRepository.save(lot1);
        lotRepository.save(lot2);

        // When
        List<Lot> lots = lotRepository.findAll();

        // Then
        assertThat(lots).hasSize(2);
    }

    @Test
    void testDeleteById() {
        // Given
        Lot lot = Lot.builder().name("Lot Delete").description("To Delete").build();
        lot = lotRepository.save(lot);
        Long id = lot.getId();

        // When
        lotRepository.deleteById(id);

        // Then
        Optional<Lot> deletedLot = lotRepository.findById(id);
        assertThat(deletedLot).isNotPresent();
    }

}
