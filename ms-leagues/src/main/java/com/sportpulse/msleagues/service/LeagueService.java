package com.sportpulse.msleagues.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sportpulse.msleagues.client.ApiFootballClient;
import com.sportpulse.msleagues.dto.LeagueResponseDto;
import com.sportpulse.msleagues.helper.JsonNodeParser;
import com.sportpulse.msleagues.helper.SeasonParser;
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
    private final SeasonParser seasonParser;
    private final JsonNodeParser jsonNodeParser;

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
