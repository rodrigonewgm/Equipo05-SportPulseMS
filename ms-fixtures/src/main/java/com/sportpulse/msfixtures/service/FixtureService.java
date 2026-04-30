package com.sportpulse.msfixtures.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sportpulse.msfixtures.client.ApiFootballClient;
import com.sportpulse.msfixtures.client.TeamsClient;
import com.sportpulse.msfixtures.dto.*;
import com.sportpulse.msfixtures.exception.FixtureNotFoundException;
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

    public List<FixtureResponseDto> getLiveFixtures() {
        JsonNode response = apiFootballClient.getLiveFixtures();
        return parseFixtures(response);
    }

    public List<FixtureEventDto> getFixtureEvents(Integer fixtureId) {
        // Primero verificar que el partido existe
        JsonNode fixtureCheck = apiFootballClient.getFixtureById(fixtureId);
        if (fixtureCheck == null || !fixtureCheck.has("response") ||
                fixtureCheck.get("response").isEmpty()) {
            throw new FixtureNotFoundException(fixtureId);
        }

        JsonNode response = apiFootballClient.getFixtureEvents(fixtureId);
        return parseEvents(response);
    }

    private List<FixtureEventDto> parseEvents(JsonNode response) {
        List<FixtureEventDto> result = new ArrayList<>();

        if (response == null || !response.has("response")) {
            return result;
        }

        for (JsonNode item : response.get("response")) {
            JsonNode teamNode = item.get("team");
            JsonNode playerNode = item.get("player");
            JsonNode assistNode = item.get("assist");

            TeamInFixtureDto team = TeamInFixtureDto.builder()
                    .id(teamNode.get("id").asInt())
                    .name(teamNode.get("name").asText())
                    .build();

            PlayerDto player = PlayerDto.builder()
                    .id(playerNode.has("id") && !playerNode.get("id").isNull() ?
                            playerNode.get("id").asInt() : null)
                    .name(playerNode.has("name") && !playerNode.get("name").isNull() ?
                            playerNode.get("name").asText() : null)
                    .build();

            PlayerDto assist = null;
            if (assistNode != null && !assistNode.isNull() &&
                    assistNode.has("name") && !assistNode.get("name").isNull()) {
                assist = PlayerDto.builder()
                        .id(assistNode.has("id") && !assistNode.get("id").isNull() ?
                                assistNode.get("id").asInt() : null)
                        .name(assistNode.get("name").asText())
                        .build();
            }

            result.add(FixtureEventDto.builder()
                    .elapsed(item.has("time") ? item.get("time").get("elapsed").asInt() : null)
                    .type(item.has("type") ? item.get("type").asText() : null)
                    .detail(item.has("detail") ? item.get("detail").asText() : null)
                    .team(team)
                    .player(player)
                    .assist(assist)
                    .build());
        }

        return result;
    }
}
