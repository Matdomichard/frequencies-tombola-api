package com.frequencies.tombola.controller;


import com.frequencies.tombola.entity.Ticket;
import com.frequencies.tombola.service.LotteryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/lottery")
public class LotteryController {

    private final LotteryService lotteryService;

    public LotteryController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @PostMapping("/draw")
    public ResponseEntity<String> drawWinner() {
        Optional<Ticket> winner = lotteryService.drawWinner();
        return winner
                .map(ticket -> ResponseEntity.ok("Le gagnant est : " + ticket.getPlayerName()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun ticket disponible pour le tirage."));
    }
}

