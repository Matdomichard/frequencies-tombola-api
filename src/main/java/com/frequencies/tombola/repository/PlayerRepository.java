package com.frequencies.tombola.repository;

import com.frequencies.tombola.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByTombolaId(Long tombolaId);
}
