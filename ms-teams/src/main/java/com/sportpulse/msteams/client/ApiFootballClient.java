package com.sportpulse.msteams.client;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ApiFootballClient {

    private final WebClient apiFootballWebClient;

    public JsonNode getTeams(Integer league, Integer season){
        return apiFootballWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/teams")
                        .queryParam("league", league)
                        .queryParam("season", season)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
}
