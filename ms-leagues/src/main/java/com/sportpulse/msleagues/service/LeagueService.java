package com.sportpulse.msleagues.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sportpulse.msleagues.client.ApiFootballClient;
import com.sportpulse.msleagues.dto.LeagueResponseDto;
import com.sportpulse.msleagues.mapper.LeagueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final ApiFootballClient apiFootballClient;
    private final LeagueMapper leagueMapper;

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

            result.add(leagueMapper.toDto(
                    league.get("id").asInt(),
                    league.get("name").asText(),
                    league.get("type").asText(),
                    country.get("name").asText(),
                    league.get("logo").asText(),
                    currentSeasonYear,
                    startDate,
                    endDate
            ));
        }

        return result;
    }
}
