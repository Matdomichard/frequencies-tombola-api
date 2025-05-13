package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.helloasso.HelloAssoFormDto;
import com.frequencies.tombola.dto.helloasso.HelloAssoFormsResponse;
import com.frequencies.tombola.dto.helloasso.HelloAssoParticipantDto;
import com.frequencies.tombola.service.HelloAssoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/helloasso")
@RequiredArgsConstructor
public class HelloAssoController {

    private final HelloAssoService helloAssoService;

    @GetMapping("/forms")
    public HelloAssoFormsResponse getAvailableForms() {
        return helloAssoService.getAvailableForms();
    }


    // All participants from a form (paid and free)
    @GetMapping("/participants")
    public List<HelloAssoParticipantDto> getAllParticipants(
            @RequestParam String formType,
            @RequestParam String formSlug
    ) {
        return helloAssoService.getAllParticipants(formType, formSlug);
    }

}