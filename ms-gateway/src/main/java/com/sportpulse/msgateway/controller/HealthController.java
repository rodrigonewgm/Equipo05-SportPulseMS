package com.sportpulse.msgateway.controller;

import com.sportpulse.msgateway.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService;

    @GetMapping("/health")
    public Mono<ResponseEntity<Map<String, Object>>> health() {
        return healthService.checkHealth()
                .map(ResponseEntity::ok);
    }
}