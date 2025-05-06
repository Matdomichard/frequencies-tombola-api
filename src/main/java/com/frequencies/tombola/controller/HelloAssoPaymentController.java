package com.frequencies.tombola.controller;

import com.frequencies.tombola.dto.helloasso.HelloAssoOrdersResponse;
import com.frequencies.tombola.service.HelloAssoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/helloasso/forms")
@RequiredArgsConstructor
public class HelloAssoPaymentController {

    private final HelloAssoService helloAssoService;

    @GetMapping("/{formType}/{formSlug}/payments")
    public ResponseEntity<HelloAssoOrdersResponse> getFormPayments(
            @PathVariable String formType,
            @PathVariable String formSlug,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to,
            @RequestParam(required = false) String userSearchKey,
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String continuationToken,
            @RequestParam(required = false) List<String> states,
            @RequestParam(defaultValue = "Desc") String sortOrder,
            @RequestParam(defaultValue = "Date") String sortField,
            @RequestParam(defaultValue = "false") boolean withCount
    ) {
        var resp = helloAssoService.getPayments(
                formType, formSlug, from, to,
                userSearchKey, pageIndex, pageSize,
                continuationToken, states,
                sortOrder, sortField, withCount
        );
        return ResponseEntity.ok(resp);
    }
}
