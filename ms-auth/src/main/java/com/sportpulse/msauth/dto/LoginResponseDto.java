package com.sportpulse.msauth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LoginResponseDto {
    private String token;
    private String tokenType;
    private long expiresIn;
    private String userId;
}
