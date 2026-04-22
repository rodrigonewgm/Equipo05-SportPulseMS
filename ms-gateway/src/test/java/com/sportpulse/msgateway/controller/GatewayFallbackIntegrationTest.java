package com.sportpulse.msgateway.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@DisplayName("FallbackController - Respuesta 503 cuando servicio no disponible")
class GatewayFallbackIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    //POR AHORA, SOLO TEST FALLBACK
    @Test
    void should_trigger_fallback_when_service_is_down() {
        webTestClient.get()
                .uri("/auth/login")
                .exchange()
                .expectStatus().isEqualTo(503)
                .expectBody()
                .jsonPath("$.service").isEqualTo("ms-auth");
    }

    // podrian haber mas test de comportamientos del sistema pero requiere configuraciones extra o WireMock
}