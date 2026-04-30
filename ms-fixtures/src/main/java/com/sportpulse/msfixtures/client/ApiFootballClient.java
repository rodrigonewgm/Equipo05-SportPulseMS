package com.sportpulse.msfixtures.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.sportpulse.msfixtures.exception.FixtureNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ApiFootballClient {

    private final WebClient apiFootballWebClient;

    public JsonNode getFixtures(Integer league, Integer team, String date, String status) {
        return apiFootballWebClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/fixtures");
                    if (league != null) uriBuilder.queryParam("league", league);
                    if (team != null) uriBuilder.queryParam("team", team);
                    if (date != null) uriBuilder.queryParam("date", date);
                    if (status != null) uriBuilder.queryParam("status", status);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    public JsonNode getFixtureById(Integer fixtureId) {
        return apiFootballWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fixtures")
                        .queryParam("id", fixtureId)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
    public JsonNode getLiveFixtures() {
        return apiFootballWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fixtures")
                        .queryParam("live", "all")
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    public JsonNode getFixtureEvents(Integer fixtureId) {
        return apiFootballWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fixtures/events")
                        .queryParam("fixture", fixtureId)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
}
