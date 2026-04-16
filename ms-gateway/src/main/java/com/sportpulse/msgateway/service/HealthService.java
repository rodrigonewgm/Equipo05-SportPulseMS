package com.sportpulse.msgateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class HealthService {

    private final WebClient webClient;

    // Lee las URLs del application.yml (igual que las rutas del gateway)
    @Value("${AUTH_SERVICE_URL:http://localhost:8081}")
    private String authUrl;

    @Value("${LEAGUES_SERVICE_URL:http://localhost:8082}")
    private String leaguesUrl;

    @Value("${TEAMS_SERVICE_URL:http://localhost:8083}")
    private String teamsUrl;

    @Value("${FIXTURES_SERVICE_URL:http://localhost:8085}")
    private String fixturesUrl;

    @Value("${STANDINGS_SERVICE_URL:http://localhost:8086}")
    private String standingsUrl;

    @Value("${NOTIFICATIONS_SERVICE_URL:http://localhost:8088}")
    private String notificationsUrl;

    @Value("${DASHBOARD_SERVICE_URL:http://localhost:8089}")
    private String dashboardUrl;

    public HealthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<Map<String, Object>> checkHealth() {
        // Llama a /actuator/health de cada servicio en paralelo
        Mono<String> authStatus       = ping(authUrl);
        Mono<String> leaguesStatus    = ping(leaguesUrl);
        Mono<String> teamsStatus      = ping(teamsUrl);
        Mono<String> fixturesStatus   = ping(fixturesUrl);
        Mono<String> standingsStatus  = ping(standingsUrl);
        Mono<String> notifStatus      = ping(notificationsUrl);
        Mono<String> dashboardStatus  = ping(dashboardUrl);

        return Mono.zip(authStatus, leaguesStatus, teamsStatus,
                        fixturesStatus, standingsStatus, notifStatus, dashboardStatus)
                .map(tuple -> {
                    Map<String, Object> services = new LinkedHashMap<>();
                    services.put("ms-auth",          tuple.getT1());
                    services.put("ms-leagues",        tuple.getT2());
                    services.put("ms-teams",          tuple.getT3());
                    services.put("ms-fixtures",       tuple.getT4());
                    services.put("ms-standings",      tuple.getT5());
                    services.put("ms-notifications",  tuple.getT6());
                    services.put("ms-dashboard",      tuple.getT7());

                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("gateway",   "UP");
                    response.put("timestamp", Instant.now().toString());
                    response.put("services",  services);
                    return response;
                });
    }

    // Si el servicio responde → "UP", si no → "DOWN"
    private Mono<String> ping(String baseUrl) {
        return webClient.get()
                .uri(baseUrl + "/actuator/health")
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(2))
                .map(body -> "UP")
                .onErrorReturn("DOWN");
    }
}