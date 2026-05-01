package com.sportpulse.msgateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({RateLimitIntegrationTest.FixedClockConfig.class, RateLimitIntegrationTest.TestEchoController.class})
@DisplayName("RateLimiter por IP - integración")
class RateLimitIntegrationTest {

    private static final String LIMIT_EXCEEDED_IP = "198.51.100.10";
    private static final String NORMAL_IP = "198.51.100.11";
    private static final String FIXED_INSTANT = "2025-01-15T10:30:30Z";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Debe permitir 60 requests por IP y bloquear la 61 con 429 y body exacto")
    void shouldAllow60RequestsAndBlock61st() {
        for (int i = 1; i <= 60; i++) {
            webTestClient.get()
                    .uri("/test/ping")
                    .header("X-Forwarded-For", LIMIT_EXCEEDED_IP)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_PLAIN)
                    .expectBody(String.class)
                    .isEqualTo("pong");
        }

        webTestClient.get()
                .uri("/test/ping")
                .header("X-Forwarded-For", LIMIT_EXCEEDED_IP)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.TOO_MANY_REQUESTS)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectHeader().valueEquals("Retry-After", "30")
                .expectBody()
                .jsonPath("$.error").isEqualTo("RATE_LIMIT_EXCEEDED")
                .jsonPath("$.message").isEqualTo("Demasiadas peticiones. Límite: 60 req/min")
                .jsonPath("$.retryAfter").isEqualTo(30)
                .jsonPath("$.timestamp").isEqualTo(FIXED_INSTANT);
    }

    @Test
    @DisplayName("Debe aplicar el límite por IP y permitir otra IP independiente")
    void shouldTreatDifferentIpsIndependently() {
        for (int i = 1; i <= 60; i++) {
            webTestClient.get()
                    .uri("/test/ping")
                    .header("X-Forwarded-For", LIMIT_EXCEEDED_IP)
                    .exchange()
                    .expectStatus().isOk();
        }

        webTestClient.get()
                .uri("/test/ping")
                .header("X-Forwarded-For", NORMAL_IP)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("pong");
    }

    @TestConfiguration
    static class FixedClockConfig {

        @Bean
        @Primary
        Clock fixedClock() {
            return Clock.fixed(Instant.parse(FIXED_INSTANT), ZoneOffset.UTC);
        }
    }

    @RestController
    static class TestEchoController {

        @GetMapping("/test/ping")
        public String ping() {
            return "pong";
        }
    }
}




