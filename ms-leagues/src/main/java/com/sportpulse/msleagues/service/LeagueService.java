package com.sportpulse.msleagues.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sportpulse.msleagues.client.ApiFootballClient;
import com.sportpulse.msleagues.dto.LeagueResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final ApiFootballClient apiFootballClient;

    @Cacheable(value = "leagues", key = "#country + '-' + #season")
    public List<LeagueResponseDto> getLeagues(String country, Integer season) {
        JsonNode response = apiFootballClient.getLeagues(country, season);
        return parseLeagues(response);
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

            String startDate = null;
            String endDate = null;
            Integer currentSeasonYear = null;

            if (seasons != null && seasons.isArray()) {
                for (JsonNode s : seasons) {
                    if (s.has("current") && s.get("current").asBoolean()) {
                        currentSeasonYear = s.get("year").asInt();
                        if (s.has("start")) startDate = s.get("start").asText();
                        if (s.has("end")) endDate = s.get("end").asText();
                        break;
                    }
                }
            }

            result.add(LeagueResponseDto.builder()
                    .id(league.get("id").asInt())
                    .name(league.get("name").asText())
                    .type(league.get("type").asText())
                    .country(country.get("name").asText())
                    .logo(league.get("logo").asText())
                    .currentSeason(currentSeasonYear)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build());
        }

        return result;
    }
}
