package com.frequencies.tombola.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tombolas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tombola {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /** the HelloAsso form slug (e.g. tombola-1) */
    private String helloAssoFormSlug;

    /** the HelloAsso form “type” (e.g. Event, PaymentForm…) */
    @Builder.Default
    private String helloAssoFormType = "Event";

    /** default creation timestamp */
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    /** whether the tombola is active */
    @Builder.Default
    private boolean active = false;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
