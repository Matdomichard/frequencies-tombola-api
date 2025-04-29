package com.frequencies.tombola.controller;

import com.frequencies.tombola.service.impl.HelloAssoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for interacting with HelloAsso API.
 */
@RestController
@RequestMapping("/api/helloasso")
@RequiredArgsConstructor
public class HelloAssoController {

    private final HelloAssoService helloAssoService;

    /**
     * Endpoint to retrieve HelloAsso forms.
     */
    @GetMapping("/forms")
    public String getForms() {
        return helloAssoService.getForms();
    }
}
