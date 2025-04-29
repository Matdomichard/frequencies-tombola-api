package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.TicketDto;
import com.frequencies.tombola.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lottery")
public class LotteryController {

    @Autowired
    private LotteryService lotteryService;

    @PostMapping("/draw")
    public ResponseEntity<TicketDto> drawWinner() {
        return lotteryService.drawWinner()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
