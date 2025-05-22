package com.frequencies.tombola.repository;

import com.frequencies.tombola.entity.Lot;
import com.frequencies.tombola.entity.Tombola;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private TombolaRepository tombolaRepository;

    private Tombola tombola;

    @BeforeEach
    void setUp() {
        tombola = Tombola.builder()
                .name("Test Tombola")
                .active(true)
                .build();
        tombola = tombolaRepository.save(tombola);
    }

    private Lot createLot(String name) {
        return Lot.builder()
                .name(name)
                .donorEmail("test@email.com")
                .tombola(tombola)
                .build();
    }

    @Test
    void testSaveLot() {
        Lot lot = createLot("Lot Test");
        Lot savedLot = lotRepository.save(lot);

        assertThat(savedLot.getId()).isNotNull();
        assertThat(savedLot.getName()).isEqualTo("Lot Test");
        assertThat(savedLot.getDonorEmail()).isEqualTo("test@email.com");
        assertThat(savedLot.getTombola()).isNotNull();
    }

    @Test
    void testFindById() {
        Lot lot = createLot("Lot Find");
        Lot savedLot = lotRepository.save(lot);

        Optional<Lot> foundLot = lotRepository.findById(savedLot.getId());
        assertThat(foundLot).isPresent();
        assertThat(foundLot.get().getName()).isEqualTo("Lot Find");
    }

    @Test
    void testFindAll() {
        Lot lot1 = createLot("Lot 1");
        Lot lot2 = createLot("Lot 2");
        lotRepository.save(lot1);
        lotRepository.save(lot2);

        List<Lot> lots = lotRepository.findAll();
        assertThat(lots.size()).isGreaterThanOrEqualTo(2);
        assertThat(lots.stream().map(Lot::getName)).contains("Lot 1", "Lot 2");
    }

    @Test
    void testDeleteById() {
        Lot lot = createLot("Lot Delete");
        Lot savedLot = lotRepository.save(lot);
        Long id = savedLot.getId();

        lotRepository.deleteById(id);

        Optional<Lot> deletedLot = lotRepository.findById(id);
        assertThat(deletedLot).isNotPresent();
    }
}
