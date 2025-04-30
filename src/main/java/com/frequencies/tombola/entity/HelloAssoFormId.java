package com.frequencies.tombola.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents the HelloAsso form identifier associated with a specific tombola.
 */
@Entity
@Table(name = "helloasso_form_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelloAssoFormId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Associated tombola
    @OneToOne(optional = false)
    @JoinColumn(name = "tombola_id", unique = true)
    private Tombola tombola;

    // HelloAsso form slug (e.g. event name or custom identifier)
    @Column(nullable = false)
    private String formSlug;

    // Type of HelloAsso form (e.g. 'event', 'form', etc.)
    @Column(nullable = false)
    private String formType;
}
