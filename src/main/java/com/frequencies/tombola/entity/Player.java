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

    /** Has this player collected their prize yet? */
    @Builder.Default
    private boolean hasCollectedPrize = false;

    /** The number of tickets by plaer */
    @Column(name = "ticket_number")
    private Integer ticketNumber;

    /** The prize assigned to this player (could be name or JSON blob) */
    private String assignedPrize;

    /** Has the “congratulations” email been sent to them? */
    @Builder.Default
    private boolean emailSent = false;

    /** Back‑reference to the tombola they belong to */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tombola_id", nullable = false)
    private Tombola tombola;
}
