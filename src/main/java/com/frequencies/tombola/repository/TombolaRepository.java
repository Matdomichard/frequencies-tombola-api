package com.frequencies.tombola.repository;

import com.frequencies.tombola.entity.Tombola;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TombolaRepository extends JpaRepository<Tombola, Long> {
}
