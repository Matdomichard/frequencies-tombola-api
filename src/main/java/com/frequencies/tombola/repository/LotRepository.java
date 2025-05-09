// src/main/java/com/frequencies/tombola/repository/LotRepository.java
package com.frequencies.tombola.repository;

import com.frequencies.tombola.entity.Lot;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository for Lot.
 */
public interface LotRepository extends JpaRepository<Lot, Long> {
    List<Lot> findByTombola_Id(Long tombolaId);
    List<Lot> findByAssignedTo_Id(Long playerId);
}


