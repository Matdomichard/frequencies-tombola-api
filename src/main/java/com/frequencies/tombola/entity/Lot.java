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

    // Name of the prize (e.g. "Nintendo Switch")
    private String name;

    // Contact's first name (donor)
    private String donorFirstName;

    // Contact's last name (donor)
    private String donorLastName;

    // Company, association or shop
    private String donorCompany;

    // Contact's email (required)
    @Column(nullable = false)
    private String donorEmail;

    // Contact's phone number (optional)
    private String donorPhone;

    // Prize value in euros (attention au mot réservé !)
    @Column(name = "lot_value", precision = 10, scale = 2)
    private BigDecimal value;

    // URL to an image of the prize
    private String imageUrl;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tombola_id")
    private Tombola tombola;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "player_id")
    private Player assignedTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private LotStatus status = LotStatus.UNASSIGNED;

    @Column(nullable = false)
    @Builder.Default
    private boolean claimed = false;
}
