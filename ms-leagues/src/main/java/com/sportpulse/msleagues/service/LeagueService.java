package com.sportpulse.msleagues.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sportpulse.msleagues.client.ApiFootballClient;
import com.sportpulse.msleagues.dto.ApiFootballResponse;
import com.sportpulse.msleagues.dto.ApiLeagueWrapper;
import com.sportpulse.msleagues.dto.LeagueDetailResponseDto;
import com.sportpulse.msleagues.dto.LeagueResponseDto;
import com.sportpulse.msleagues.exception.LeagueNotFoundException;
import com.sportpulse.msleagues.helper.JsonNodeParser;
import com.sportpulse.msleagues.helper.SeasonParser;
import com.sportpulse.msleagues.mapper.LeagueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final ApiFootballClient apiFootballClient;
    private final LeagueMapper leagueMapper;
    private final SeasonParser seasonParser;
    private final JsonNodeParser jsonNodeParser;
    private final RestTemplate restTemplate;
    private final Map<Integer, LeagueDetailResponseDto> cache = new ConcurrentHashMap<>();

    @Value("${rapidapi.base-url}")
    private String baseUrl;

    @Value("${rapidapi.key}")
    private String apiKey;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Cacheable(value = "leagues", key = "#country + '-' + #season")
    public List<LeagueResponseDto> getLeagues(String country, Integer season) {
        JsonNode response = apiFootballClient.getLeagues(country, season);
        return parseLeagues(response);
    }

    public LeagueDetailResponseDto getLeagueDetail(Integer leagueId, String authHeader) {
        validateToken(authHeader);

        if (cache.containsKey(leagueId)) {
            return cache.get(leagueId);
        }

        String url = baseUrl + "/leagues?id=" + leagueId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ApiFootballResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, ApiFootballResponse.class);

        if (response.getBody() == null || response.getBody().getResponse().isEmpty()) {
            throw new LeagueNotFoundException();
        }

        ApiLeagueWrapper wrapper = response.getBody().getResponse().get(0);
        LeagueDetailResponseDto dto = leagueMapper.toDetailDto(wrapper);
        cache.put(leagueId, dto);
        return dto;
    }

    public void validateToken(String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                authServiceUrl, HttpMethod.POST, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("UNAUTHORIZED");
        }
    }

    private List<LeagueResponseDto> parseLeagues(JsonNode response) {
        List<LeagueResponseDto> result = new ArrayList<>();

        if (response == null || !response.has("response")) {
            return result;
        }

        for (JsonNode item : response.get("response")) {
            JsonNode league = item.get("league");
            JsonNode country = item.get("country");
            JsonNode seasons = item.get("seasons");

            result.add(leagueMapper.toDto(
                    jsonNodeParser.getInt(league, "id"),
                    jsonNodeParser.getText(league, "name"),
                    jsonNodeParser.getText(league, "type"),
                    jsonNodeParser.getText(country, "name"),
                    jsonNodeParser.getText(league, "logo"),
                    seasonParser.getCurrentSeasonYear(seasons),
                    seasonParser.getCurrentSeasonStart(seasons),
                    seasonParser.getCurrentSeasonEnd(seasons)
            ));
        }

        return result;
    }
}
