package com.sportpulse.msteams.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sportpulse.msteams.client.ApiFootballClient;
import com.sportpulse.msteams.dto.StadiumDetailDto;
import com.sportpulse.msteams.dto.StadiumDto;
import com.sportpulse.msteams.dto.TeamDetailResponseDto;
import com.sportpulse.msteams.dto.TeamResponseDto;
import com.sportpulse.msteams.exception.TeamNotFoundException;
import com.sportpulse.msteams.mapper.TeamDetailMapper;
import com.sportpulse.msteams.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final ApiFootballClient apiFootballClient;
    private final TeamMapper teamMapper;
    private final TeamDetailMapper teamDetailMapper;


    public List<TeamResponseDto> getTeams(Integer league, Integer season) {
        JsonNode response = apiFootballClient.getTeams(league, season);
        return parseTeams(response);
    }

    private List<TeamResponseDto> parseTeams(JsonNode response) {
        List<TeamResponseDto> result = new ArrayList<>();

        if (response == null || !response.has("response")) {
            return result;
        }

        for (JsonNode item : response.get("response")) {
            JsonNode team = item.get("team");
            JsonNode venue = item.get("venue");

            StadiumDto stadium = StadiumDto.builder()
                    .name(venue.has("name") ? venue.get("name").asText() : null)
                    .city(venue.has("city") ? venue.get("city").asText() : null)
                    .capacity(venue.has("capacity") ? venue.get("capacity").asInt() : null)
                    .build();

            result.add(teamMapper.toDto(
                    team.get("id").asInt(),
                    team.get("name").asText(),
                    team.get("country").asText(),
                    team.get("logo").asText(),
                    team.has("founded") ? team.get("founded").asInt() : null,
                    stadium
            ));
        }

        return result;
    }

    public TeamDetailResponseDto getTeamById(Integer teamId) {
        JsonNode response = apiFootballClient.getTeamById(teamId);

        if (response == null || !response.has("response") ||
                response.get("response").isEmpty()) {
            throw new TeamNotFoundException(teamId);
        }

        JsonNode item = response.get("response").get(0);
        JsonNode team = item.get("team");
        JsonNode venue = item.get("venue");

        StadiumDetailDto stadium = StadiumDetailDto.builder()
                .name(venue.has("name") ? venue.get("name").asText() : null)
                .address(venue.has("address") ? venue.get("address").asText() : null)
                .city(venue.has("city") ? venue.get("city").asText() : null)
                .capacity(venue.has("capacity") ? venue.get("capacity").asInt() : null)
                .surface(venue.has("surface") ? venue.get("surface").asText() : null)
                .build();

        return teamDetailMapper.toDto(
                team.get("id").asInt(),
                team.get("name").asText(),
                team.get("country").asText(),
                team.get("logo").asText(),
                team.has("founded") ? team.get("founded").asInt() : null,
                team.has("national") ? team.get("national").asBoolean() : null,
                stadium
        );
    }
}
