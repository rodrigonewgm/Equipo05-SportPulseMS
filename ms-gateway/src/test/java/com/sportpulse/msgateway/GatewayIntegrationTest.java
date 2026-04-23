package com.sportpulse.msgateway;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
/*
Decido dejar el contexto limpio después de cada test para evitar que el estado del circuito
(abierto/cerrado) afecte a otros tests.
No uso CircuitRegistry con reset() porque no es tan confiable para limpiar el estado entre tests,
y prefiero asegurarme de que cada test arranque con un contexto completamente nuevo.
Esto hace que los pocos tests sean más independientes y predecibles, aunque puede hacer que
tarden un poco más en ejecutarse.
 */
class GatewayIntegrationTest {

    private static MockWebServer mockServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("AUTH_SERVICE_URL", () -> mockServer.url("/").toString());

        // Configuramos el circuito para que abra rápido (1 llamada fallida) y tenga una ventana pequeña (2 llamadas) como en la app
        registry.add("resilience4j.circuitbreaker.instances.ms-auth-cb.minimumNumberOfCalls", () -> "1");
        registry.add("resilience4j.circuitbreaker.instances.ms-auth-cb.slidingWindowSize", () -> "2");
        registry.add("resilience4j.circuitbreaker.instances.ms-auth-cb.waitDurationInOpenState", () -> "1s");
    }

    @Autowired
    private WebTestClient webTestClient;

    @AfterAll
    static void stopServer() throws IOException {
        mockServer.shutdown();
    }

    @Test
    void whenDownstreamReturns200_thenGatewayReturns200() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"ok\":true}")
                .addHeader("Content-Type", "application/json"));

        webTestClient.get().uri("/api/auth/ok")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody()
                .jsonPath("$.ok").isEqualTo(true);
    }

    @Test
    void whenDownstreamFails_thenCircuitBreakerOpensAndFallbackCalled(){
        // Encolamos una desconexion para abrir el circuito
        mockServer.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START));

        // Llamada 1: Falla (500), -> Se abre el circuito
        webTestClient.get().uri("/api/auth/test").exchange().expectStatus().is5xxServerError();

        // Llamada 2: El circuito ya está abierto -> Fallback (503)
        webTestClient.get().uri("/api/auth/test")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.SERVICE_UNAVAILABLE)
                .expectBody()
                .jsonPath("$.service").isEqualTo("ms-auth");
    }

    @Test
    void whenCircuitOpen_andWaitTimePasses_thenCircuitClosesOnSuccess() throws InterruptedException {
        // Abro circuito con una llamada fallida
        mockServer.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START));
        webTestClient.get().uri("/api/auth/test").exchange().expectStatus().is5xxServerError();

        // Esperar que pase el waitDurationInOpenState (1s en este caso)
        Thread.sleep(1100);

        // El próximo llamado exitoso debería cerrar el circuito
        mockServer.enqueue(new MockResponse().setResponseCode(200).setBody("{\"status\":\"recovered\"}"));

        webTestClient.get().uri("/api/auth/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.status").isEqualTo("recovered");
    }
}

