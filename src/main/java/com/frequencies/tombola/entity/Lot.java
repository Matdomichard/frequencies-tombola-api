package com.frequencies.tombola.entity;

import jakarta.persistence.*;
import lombok.*;

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

    // Description of the prize
    private String description;

    // Associated tombola
    @ManyToOne(optional = false)
    @JoinColumn(name = "tombola_id")
    private Tombola tombola;

    // Whether the prize has been claimed by the winner
    @Column(nullable = false)
    @Builder.Default
    private boolean claimed = false;
}
