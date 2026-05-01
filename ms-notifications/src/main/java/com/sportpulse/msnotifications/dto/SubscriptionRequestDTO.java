package com.sportpulse.msnotifications.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubscriptionRequestDTO {

    private Integer teamId;

    private List<String> events; // MATCH_START, GOAL, etc.

    private String channel; // WEBHOOK o LOG

    private String webhookUrl;

    private String type;
}