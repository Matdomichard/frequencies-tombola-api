package com.frequencies.tombola.repository;

import java.util.List;

import com.frequencies.tombola.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByIsWinnerFalse();
    List<Ticket> findByIsWinnerTrue();
}

