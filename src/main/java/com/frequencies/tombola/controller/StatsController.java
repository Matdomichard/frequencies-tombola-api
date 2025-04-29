package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.StatsDto;
import com.frequencies.tombola.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    public StatsDto getStats() {
        return statsService.getStats();
    }
}
