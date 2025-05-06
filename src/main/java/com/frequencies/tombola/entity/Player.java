package com.frequencies.tombola.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "players")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    private String phoneNumber;
    @Builder.Default
    private boolean hasCollectedPrize = false;

    // lien vers la tombola
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tombola_id", nullable = false)
    private Tombola tombola;
}
