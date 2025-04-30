package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.helloasso.HelloAssoParticipantDto;
import com.frequencies.tombola.service.HelloAssoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for interacting with HelloAsso API.
 */
@RestController
@RequestMapping("/helloasso")
@RequiredArgsConstructor
public class HelloAssoController {

    private final HelloAssoService helloAssoService;

    /**
     * Get participants who paid on HelloAsso for a given form.
     * Example URL: /helloasso/participants?formType=events&formSlug=tombola-1
     */
    @GetMapping("/participants")
    public List<HelloAssoParticipantDto> getPaidParticipants(
            @RequestParam String formType,
            @RequestParam String formSlug
    ) {
        return helloAssoService.getPaidParticipants(formType, formSlug);
    }
}
