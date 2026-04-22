package com.sportpulse.msgateway.controller;

import com.sportpulse.msgateway.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @RequestMapping("/fallback/{serviceName}")
    public ResponseEntity<ErrorResponseDto> fallback(@PathVariable String serviceName,
                                                        ServerHttpRequest request) {

        ErrorResponseDto body = ErrorResponseDto.serviceUnavailable(
                serviceName,
                request.getURI().getPath()
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }
}