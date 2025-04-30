package com.frequencies.tombola.service;

import com.frequencies.tombola.dto.helloasso.HelloAssoParticipantDto;

import java.util.List;

public interface HelloAssoService {

    /**
     * Retrieves all participants who completed payment
     * for a given HelloAsso form (event, membership, etc.).
     *
     * @param formType The type of HelloAsso form (e.g. "events", "memberships").
     * @param formSlug The unique slug of the form.
     * @return List of participants who paid.
     */
    List<HelloAssoParticipantDto> getPaidParticipants(String formType, String formSlug);
}
