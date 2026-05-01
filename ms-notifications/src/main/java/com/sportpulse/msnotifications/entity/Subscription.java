package com.sportpulse.msnotifications.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String userId;

    private Integer teamId;

    @ElementCollection
    private List<String> events; // MATCH_START, GOAL, etc.

    private String channel; // WEBHOOK o LOG

    private String webhookUrl; // solo si es WEBHOOK

    private boolean active = true;

    private String type;

    private Instant createdAt;
}
