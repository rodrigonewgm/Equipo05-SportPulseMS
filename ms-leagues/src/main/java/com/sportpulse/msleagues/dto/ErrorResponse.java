package com.sportpulse.msleagues.dto;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String error;
    private String message;
    private Instant timestamp;
}
