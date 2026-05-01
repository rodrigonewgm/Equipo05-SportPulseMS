package com.sportpulse.msgateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"error", "message", "retryAfter", "timestamp"})
public record RateLimitExceededResponseDto(
        String error,
        String message,
        Integer retryAfter,
        String timestamp
) {
    public static RateLimitExceededResponseDto tooManyRequests(int limitPerMinute, int retryAfterSeconds, String timestamp) {
        return new RateLimitExceededResponseDto(
                "RATE_LIMIT_EXCEEDED",
                "Demasiadas peticiones. Límite: " + limitPerMinute + " req/min",
                retryAfterSeconds,
                timestamp
        );
    }
}


