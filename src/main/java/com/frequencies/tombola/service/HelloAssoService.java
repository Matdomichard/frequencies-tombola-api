package com.frequencies.tombola.service;

import com.frequencies.tombola.dto.helloasso.*;
import java.time.OffsetDateTime;
import java.util.List;

public interface HelloAssoService {
    List<HelloAssoParticipantDto> getPaidParticipants(String formType, String formSlug);
    List<HelloAssoFormDto> getAvailableForms();

    // New method to fetch payments with all query params
    HelloAssoOrdersResponse getPayments(
            String formType,
            String formSlug,
            OffsetDateTime from,
            OffsetDateTime to,
            String userSearchKey,
            int pageIndex,
            int pageSize,
            String continuationToken,
            List<String> states,
            String sortOrder,
            String sortField,
            boolean withCount
    );
}
