package com.sportpulse.msauth.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class RegisterResponseDto {
    private UUID id;
    private String username;
    private String email;
    private String role;
    private Instant createdAt;
}
