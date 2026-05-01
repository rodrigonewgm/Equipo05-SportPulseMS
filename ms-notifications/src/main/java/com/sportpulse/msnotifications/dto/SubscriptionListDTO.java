package com.sportpulse.msnotifications.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SubscriptionListDTO {
    private UUID subscriptionId;
    private String type;
    private Integer teamId;
    private List<String> events;
    private String channel;
    private String webhookUrl;
    private String status;
}
