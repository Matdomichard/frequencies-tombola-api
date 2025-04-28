package com.frequencies.tombola.mapper;

import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.entity.Player;

public class PlayerMapper {

    public static PlayerDto toDto(Player player) {
        return PlayerDto.builder()
                .id(player.getId())
                .name(player.getName())
                .email(player.getEmail())
                .phoneNumber(player.getPhoneNumber())
                .hasCollectedPrize(player.isHasCollectedPrize())
                .build();
    }

    public static Player toEntity(PlayerDto dto) {
        return Player.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .hasCollectedPrize(dto.isHasCollectedPrize())
                .build();
    }
}
