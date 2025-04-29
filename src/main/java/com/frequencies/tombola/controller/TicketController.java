package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.TicketDto;
import com.frequencies.tombola.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketDto>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<TicketDto>> getTicketsByPlayer(@PathVariable Long playerId) {
        return ResponseEntity.ok(ticketService.getTicketsByPlayerId(playerId));
    }

    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@RequestBody TicketDto ticketDto) {
        return ResponseEntity.ok(ticketService.createTicket(ticketDto));
    }

    @PutMapping("/{id}/winner")
    public ResponseEntity<TicketDto> markTicketAsWinner(@PathVariable Long id) {
        return ticketService.markTicketAsWinner(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/tickets/{id}/claim")
    public ResponseEntity<TicketDto> claimPrize(@PathVariable Long id) {
        return ticketService.claimPrize(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/winners")
    public List<TicketDto> getWinningTickets() {
        return ticketService.getWinningTickets();
    }

}
