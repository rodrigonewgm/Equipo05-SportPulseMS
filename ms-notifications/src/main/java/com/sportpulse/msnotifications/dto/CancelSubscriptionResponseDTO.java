package com.sportpulse.msnotifications.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CancelSubscriptionResponseDTO {
    private UUID subscriptionId;
    private String status;
    private Instant cancelledAt;
}
