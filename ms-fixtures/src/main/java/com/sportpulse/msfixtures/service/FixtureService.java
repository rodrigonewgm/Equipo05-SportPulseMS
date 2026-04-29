package com.sportpulse.msfixtures.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sportpulse.msfixtures.client.ApiFootballClient;
import com.sportpulse.msfixtures.client.TeamsClient;
import com.sportpulse.msfixtures.dto.*;
import com.sportpulse.msfixtures.mapper.FixtureMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FixtureService {

    private final ApiFootballClient apiFootballClient;
    private final TeamsClient teamsClient;
    private final FixtureMapper fixtureMapper;

    public List<FixtureResponseDto> getFixtures(Integer league, Integer team,
                                                String date, String status) {
        // Si no se proporciona ningún filtro, usar la fecha de hoy
        String effectiveDate = date;
        if (league == null && team == null && date == null && status == null) {
            effectiveDate = LocalDate.now().toString();
        }

        JsonNode response = apiFootballClient.getFixtures(league, team, effectiveDate, status);
        return parseFixtures(response);
    }

    private List<FixtureResponseDto> parseFixtures(JsonNode response) {
        List<FixtureResponseDto> result = new ArrayList<>();

        if (response == null || !response.has("response")) {
            return result;
        }

        for (JsonNode item : response.get("response")) {
            JsonNode fixture = item.get("fixture");
            JsonNode league = item.get("league");
            JsonNode teams = item.get("teams");
            JsonNode goals = item.get("goals");

            JsonNode homeTeamNode = teams.get("home");
            JsonNode awayTeamNode = teams.get("away");

            Integer homeId = homeTeamNode.get("id").asInt();
            Integer awayId = awayTeamNode.get("id").asInt();

            // Enriquecer con logo de ms-teams
            String homeLogo = teamsClient.getTeamLogo(homeId);
            String awayLogo = teamsClient.getTeamLogo(awayId);

            // Usar logo de API-Football como fallback
            if (homeLogo == null) homeLogo = homeTeamNode.has("logo") ?
                    homeTeamNode.get("logo").asText() : null;
            if (awayLogo == null) awayLogo = awayTeamNode.has("logo") ?
                    awayTeamNode.get("logo").asText() : null;

            TeamInFixtureDto homeTeam = TeamInFixtureDto.builder()
                    .id(homeId)
                    .name(homeTeamNode.get("name").asText())
                    .logo(homeLogo)
                    .goals(goals.get("home").isNull() ? null : goals.get("home").asInt())
                    .build();

            TeamInFixtureDto awayTeam = TeamInFixtureDto.builder()
                    .id(awayId)
                    .name(awayTeamNode.get("name").asText())
                    .logo(awayLogo)
                    .goals(goals.get("away").isNull() ? null : goals.get("away").asInt())
                    .build();

            JsonNode statusNode = fixture.get("status");
            FixtureStatusDto statusDto = FixtureStatusDto.builder()
                    .shortStatus(statusNode.get("short").asText())
                    .longStatus(statusNode.get("long").asText())
                    .build();

            FixtureLeagueDto leagueDto = FixtureLeagueDto.builder()
                    .id(league.get("id").asInt())
                    .name(league.get("name").asText())
                    .round(league.has("round") ? league.get("round").asText() : null)
                    .build();

            JsonNode venueNode = fixture.get("venue");
            VenueDto venueDto = VenueDto.builder()
                    .name(venueNode.has("name") ? venueNode.get("name").asText() : null)
                    .city(venueNode.has("city") ? venueNode.get("city").asText() : null)
                    .build();

            result.add(fixtureMapper.toDto(
                    fixture.get("id").asInt(),
                    fixture.get("date").asText(),
                    statusDto,
                    leagueDto,
                    homeTeam,
                    awayTeam,
                    venueDto
            ));
        }

        return result;
    }
}
