package com.sportpulse.msgateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    private String error;
    private String message;
    private String service;
    private String path;
    private String timestamp;

    public static ErrorResponseDto serviceUnavailable(String serviceName, String path) {
        return ErrorResponseDto.builder()
                .error("SERVICE_UNAVAILABLE")
                .message("El servicio '" + serviceName + "' no está disponible. Intente de nuevo más tarde.")
                .service(serviceName)
                .path(path)
                .timestamp(Instant.now().toString())
                .build();
    }
}