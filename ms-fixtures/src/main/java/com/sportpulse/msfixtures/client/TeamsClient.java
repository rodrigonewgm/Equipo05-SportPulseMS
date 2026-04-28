package com.sportpulse.msfixtures.client;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class TeamsClient {

    private final WebClient teamsWebClient;

    public String getTeamLogo(Integer teamId) {
        try {
            JsonNode response = teamsWebClient.get()
                    .uri("/api/teams/" + teamId)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (response != null && response.has("logo")) {
                return response.get("logo").asText();
            }
        } catch (Exception e) {
            // Si ms-teams no está disponible, devolvemos null
        }
        return null;
    }
}
