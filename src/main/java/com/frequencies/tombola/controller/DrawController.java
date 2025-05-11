// src/main/java/com/frequencies/tombola/controller/DrawController.java
package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.DrawResultDto;
import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.dto.LotDto;
import com.frequencies.tombola.service.DrawService;
import com.frequencies.tombola.service.PlayerService;
import com.frequencies.tombola.service.LotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// src/main/java/com/frequencies/tombola/controller/DrawController.java
@RestController
@RequestMapping("/tombolas")
@RequiredArgsConstructor
public class DrawController {
    private final DrawService drawService;
    private final PlayerService playerService;
    private final LotService lotService;

    @PostMapping("/{id}/draw")
    public ResponseEntity<DrawResultDto> draw(
            @PathVariable Long id,
            @RequestParam(name = "guaranteeOneLotPerParticipant", defaultValue = "false") boolean guaranteeOneLotPerParticipant
    ) {
        DrawResultDto result = drawService.draw(id, guaranteeOneLotPerParticipant);
        return ResponseEntity.ok(result);
    }

}

