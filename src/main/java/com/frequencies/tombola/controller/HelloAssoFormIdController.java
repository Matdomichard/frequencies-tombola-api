package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.helloasso.HelloAssoFormIdDto;
import com.frequencies.tombola.service.HelloAssoFormIdService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/helloasso-form-id")
public class HelloAssoFormIdController {

    private final HelloAssoFormIdService service;

    public HelloAssoFormIdController(HelloAssoFormIdService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<HelloAssoFormIdDto> save(@RequestBody HelloAssoFormIdDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping("/{tombolaId}")
    public ResponseEntity<HelloAssoFormIdDto> getByTombolaId(@PathVariable Long tombolaId) {
        Optional<HelloAssoFormIdDto> result = service.findByTombolaId(tombolaId);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
