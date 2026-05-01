package com.sportpulse.msnotifications.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class SubscriptionResponseDTO {
    private UUID subscriptionId;
    private String userId;
    private String type;
    private Integer teamId;
    private List<String> events;
    private String channel;
    private String status;
    private Instant createdAt;
}
