package com.frequencies.tombola.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_reminder")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ticket linked to this email reminder
    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    // When the email was sent
    @Column(nullable = false)
    private LocalDateTime sentAt;

    // Reminder type (e.g. first, final)
    private String type;

    // Whether the email was sent successfully
    private boolean success;

    // Optional error message if sending failed
    private String errorMessage;
}
