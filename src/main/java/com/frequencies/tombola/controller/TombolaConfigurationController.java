package com.frequencies.tombola.controller;

import com.frequencies.tombola.service.TombolaConfigurationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
public class TombolaConfigurationController {

    private final TombolaConfigurationService configService;

    public TombolaConfigurationController(TombolaConfigurationService configService) {
        this.configService = configService;
    }

    // --- Set number of tickets ---
    @PostMapping("/tickets")
    public void setTicketsPerParticipant(@RequestParam int count) {
        configService.setTicketsPerParticipant(count);
    }

    @GetMapping("/tickets")
    public int getTicketsPerParticipant() {
        return configService.getTicketsPerParticipant();
    }

    // --- Enable/Disable Guarantee ---
    @PostMapping("/guarantee/enable")
    public void enableGuarantee() {
        configService.enableGuaranteeOneLotPerParticipant();
    }

    @PostMapping("/guarantee/disable")
    public void disableGuarantee() {
        configService.disableGuaranteeOneLotPerParticipant();
    }

    @GetMapping("/guarantee")
    public boolean isGuaranteeEnabled() {
        return configService.isGuaranteeEnabled();
    }
}
