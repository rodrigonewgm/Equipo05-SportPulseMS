package com.sportpulse.msgateway.controller;

import com.sportpulse.msgateway.service.HealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Health", description = "Estado del gateway y servicios downstream")
public class HealthController {

    private final HealthService healthService;

    @GetMapping("/health")
    @Operation(summary = "Estado del sistema", description = "Devuelve el estado del gateway y de todos los microservicios downstream")
    @ApiResponse(responseCode = "200", description = "Estado obtenido correctamente")
    public Mono<ResponseEntity<Map<String, Object>>> health() {
        return healthService.checkHealth()
                .map(ResponseEntity::ok);
    }
}