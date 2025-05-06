package com.frequencies.tombola.repository;

import com.frequencies.tombola.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    // English comment: find all players for a given tombola
    List<Player> findByTombola_Id(Long tombolaId);
}
