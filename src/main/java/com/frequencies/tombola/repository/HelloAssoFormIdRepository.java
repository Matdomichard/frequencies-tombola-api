package com.frequencies.tombola.repository;

import com.frequencies.tombola.entity.HelloAssoFormId;
import com.frequencies.tombola.entity.Tombola;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HelloAssoFormIdRepository extends JpaRepository<HelloAssoFormId, Long> {
    Optional<HelloAssoFormId> findByTombola(Tombola tombola);
}
