package com.frequencies.tombola.service;

import com.frequencies.tombola.dto.DrawResultDto;

public interface DrawService {
    DrawResultDto draw(Long tombolaId, boolean guaranteeOneLotPerParticipant);
}
