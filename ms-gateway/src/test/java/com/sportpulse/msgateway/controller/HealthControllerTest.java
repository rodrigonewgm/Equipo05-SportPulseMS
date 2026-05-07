package com.sportpulse.msgateway.controller;

import com.sportpulse.msgateway.config.RateLimitConfiguration;
import com.sportpulse.msgateway.service.HealthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@WebFluxTest(HealthController.class)
@Import(RateLimitConfiguration.class)
class HealthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private HealthService healthService;

    @Test
    void healthEndpoint_debeResponder200() {
        Map<String, Object> services = new LinkedHashMap<>();
        services.put("ms-auth", "UP");
        services.put("ms-leagues", "UP");

        Map<String, Object> mockResponse = new LinkedHashMap<>();
        mockResponse.put("gateway", "UP");
        mockResponse.put("timestamp", "2025-01-15T10:30:00Z");
        mockResponse.put("services", services);

        when(healthService.checkHealth()).thenReturn(Mono.just(mockResponse));

        webTestClient.get().uri("/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.gateway").isEqualTo("UP")
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.services").exists();
    }

    @Test
    void healthEndpoint_cuandoServicioCaido_devuelveDown() {
        Map<String, Object> services = new LinkedHashMap<>();
        services.put("ms-auth", "DOWN");
        services.put("ms-leagues", "UP");

        Map<String, Object> mockResponse = new LinkedHashMap<>();
        mockResponse.put("gateway", "UP");
        mockResponse.put("timestamp", "2025-01-15T10:30:00Z");
        mockResponse.put("services", services);

        when(healthService.checkHealth()).thenReturn(Mono.just(mockResponse));

        webTestClient.get().uri("/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.services['ms-auth']").isEqualTo("DOWN");
    }

    @Test
    void healthEndpoint_esPublico_noRequiereAuth() {
        when(healthService.checkHealth()).thenReturn(Mono.just(Map.of("gateway", "UP")));

        // Si llegara a requerir auth, devolvería 401 — acá esperamos 200
        webTestClient.get().uri("/health")
                .exchange()
                .expectStatus().isOk();
    }
}