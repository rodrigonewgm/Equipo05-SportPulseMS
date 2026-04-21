package com.sportpulse.msleagues.client;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ApiFootballClient {

    private final WebClient apiFootballWebClient;

    public JsonNode getLeagues(String country, Integer season) {
        return apiFootballWebClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/leagues");
                    if (country != null) uriBuilder.queryParam("country", country);
                    if (season != null) uriBuilder.queryParam("season", season);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
}
