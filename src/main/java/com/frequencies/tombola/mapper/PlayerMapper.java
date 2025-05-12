package com.frequencies.tombola.mapper;

import com.frequencies.tombola.dto.PlayerDto;
import com.frequencies.tombola.entity.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public PlayerDto toDto(Player player) {
        return PlayerDto.builder()
                .id(player.getId())
                .firstName(player.getFirstName())
                .lastName(player.getLastName())
                .email(player.getEmail())
                .ticketNumber(player.getTicketNumber())
                .phone(player.getPhoneNumber())
                .hasCollectedPrize(player.isHasCollectedPrize())
                .paymentMethod(player.getPaymentMethod())
                .build();
    }

    public Player toEntity(PlayerDto dto) {
        return Player.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .ticketNumber(dto.getTicketNumber())
                .phoneNumber(dto.getPhone())
                .hasCollectedPrize(dto.isHasCollectedPrize())
                .paymentMethod(dto.getPaymentMethod())
                .build();
    }
}
