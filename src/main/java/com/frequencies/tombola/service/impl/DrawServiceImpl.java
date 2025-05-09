// src/main/java/com/frequencies/tombola/service/impl/DrawServiceImpl.java
package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.dto.DrawResultDto;
import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.dto.LotDto;
import com.frequencies.tombola.entity.Lot;
import com.frequencies.tombola.entity.LotStatus;
import com.frequencies.tombola.entity.Player;
import com.frequencies.tombola.mapper.LotMapper;
import com.frequencies.tombola.mapper.PlayerMapper;
import com.frequencies.tombola.repository.LotRepository;
import com.frequencies.tombola.repository.PlayerRepository;
import com.frequencies.tombola.service.DrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrawServiceImpl implements DrawService {

    private final PlayerRepository playerRepo;
    private final LotRepository    lotRepo;
    private final PlayerMapper playerMapper;
    private final LotMapper lotMapper;
    private final Random           random = new Random();

    @Override
    @Transactional
    public DrawResultDto draw(Long tombolaId) {
        List<Player> players = playerRepo.findByTombolaId(tombolaId);
        List<Lot>    lots    = lotRepo.findByTombola_Id(tombolaId);

        if (players.isEmpty() || lots.isEmpty()) {
            return new DrawResultDto(
                    players.stream().map(playerMapper::toDto).collect(Collectors.toList()),
                    lots   .stream().map(lotMapper::toDto).collect(Collectors.toList())
            );
        }

        for (Lot lot : lots) {
            Collections.shuffle(players, random);
            Player winner = players.get(0);
            lot.setAssignedTo(winner);
            lot.setStatus(LotStatus.ASSIGNED);
            lotRepo.save(lot);
        }

        List<PlayerDto> playerDtos = playerRepo.findByTombolaId(tombolaId).stream()
                .map(playerMapper::toDto)
                .collect(Collectors.toList());
        List<LotDto> lotDtos = lotRepo.findByTombola_Id(tombolaId).stream()
                .map(lotMapper::toDto)
                .collect(Collectors.toList());

        return new DrawResultDto(playerDtos, lotDtos);
    }
}
