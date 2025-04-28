package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.LotDto;
import com.frequencies.tombola.service.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/lots")
public class LotController {

    @Autowired
    private LotService lotService;

    @GetMapping
    public ResponseEntity<List<LotDto>> getAllLots() {
        return ResponseEntity.ok(lotService.getAllLots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LotDto> getLotById(@PathVariable Long id) {
        return lotService.getLotById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LotDto> createLot(@RequestBody LotDto lotDto) {
        return ResponseEntity.ok(lotService.createLot(lotDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LotDto> updateLot(@PathVariable Long id, @RequestBody LotDto lotDto) {
        return lotService.updateLot(id, lotDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLot(@PathVariable Long id) {
        if (lotService.deleteLot(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
