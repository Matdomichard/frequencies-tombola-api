package com.frequencies.tombola.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tombola_id")
    private Tombola tombola;

    @Column(nullable = false)
    @Builder.Default
    private boolean isWinner = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean hasClaimedPrize = false;
}
