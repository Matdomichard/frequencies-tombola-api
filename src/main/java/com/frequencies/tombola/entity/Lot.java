package com.frequencies.tombola.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "lots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Prize name (e.g. "Nintendo Switch")
    private String name;

    // Detailed description of the prize
    @Column(columnDefinition = "TEXT")
    private String description;

    // Associated tombola
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tombola_id")
    private Tombola tombola;

    // Donor name (individual or organization)
    private String donorName;

    // Donor contact info (email or phone)
    private String donorContact;

    // Monetary value of the prize in euros
    @Column(precision = 10, scale = 2)
    private BigDecimal value;

    // URL to an image of the prize
    private String imageUrl;

    /**
     * A player can now receive multiple lots ⇒ ManyToOne.
     * Removed unique = true and switched to ManyToOne.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "player_id")  // no more unique = true
    private Player assignedTo;

    // Lifecycle status of the prize
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LotStatus status = LotStatus.UNASSIGNED;

    // Whether the prize has been claimed
    @Column(nullable = false)
    @Builder.Default
    private boolean claimed = false;
}
