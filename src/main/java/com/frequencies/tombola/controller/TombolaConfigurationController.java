package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.TombolaConfigurationDto;
import com.frequencies.tombola.service.TombolaConfigurationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
public class TombolaConfigurationController {

    private final TombolaConfigurationService configService;

    public TombolaConfigurationController(TombolaConfigurationService configService) {
        this.configService = configService;
    }

    @GetMapping
    public TombolaConfigurationDto getConfig() {
        return TombolaConfigurationDto.builder()
                .ticketsPerParticipant(configService.getTicketsPerParticipant())
                .guaranteeOneLotPerParticipant(configService.isGuaranteeEnabled())
                .build();
    }

    @PutMapping
    public void updateConfig(@RequestBody TombolaConfigurationDto dto) {
        if (dto.getTicketsPerParticipant() != null) {
            configService.setTicketsPerParticipant(dto.getTicketsPerParticipant());
        }
        if (dto.getGuaranteeOneLotPerParticipant() != null) {
            if (dto.getGuaranteeOneLotPerParticipant()) {
                configService.enableGuaranteeOneLotPerParticipant();
            } else {
                configService.disableGuaranteeOneLotPerParticipant();
            }
        }
    }
}
