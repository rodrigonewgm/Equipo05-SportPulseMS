package com.sportpulse.msstandings.service;

import com.sportpulse.msstandings.client.ApiFootballClient;
import com.sportpulse.msstandings.client.TeamClient;
import com.sportpulse.msstandings.dto.*;
import com.sportpulse.msstandings.external.ApiFootballStandingsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StandingsService {

    private final ApiFootballClient apiFootballClient;
    private final TeamClient teamClient;

    public StandingsResponseDTO getStandings(Integer league, Integer season, String token) {

        ApiFootballStandingsResponse apiResponse =
                apiFootballClient.getStandings(league, season);

        // 🔥 validación básica para evitar null
        if (apiResponse == null || apiResponse.getResponse() == null || apiResponse.getResponse().isEmpty()) {
            throw new RuntimeException("API-Football no devolvió datos");
        }

        ApiFootballStandingsResponse.League leagueData =
                apiResponse.getResponse().get(0).getLeague();

        List<ApiFootballStandingsResponse.Standing> standings =
                leagueData.getStandings().get(0);

        List<StandingDTO> result = standings.stream().map(s -> {

            StandingDTO dto = new StandingDTO();

            dto.setRank(s.getRank());
            dto.setPoints(s.getPoints());
            dto.setForm(s.getForm());

            if (s.getAll() != null) {
                dto.setPlayed(s.getAll().getPlayed());
                dto.setWon(s.getAll().getWin());
                dto.setDrawn(s.getAll().getDraw());
                dto.setLost(s.getAll().getLose());
            } else {
                dto.setPlayed(0);
                dto.setWon(0);
                dto.setDrawn(0);
                dto.setLost(0);
            }

            if (s.getAll() != null && s.getAll().getGoals() != null) {

                int goalsFor = s.getAll().getGoals().getForGoals();
                int goalsAgainst = s.getAll().getGoals().getAgainst();

                dto.setGoalsFor(goalsFor);
                dto.setGoalsAgainst(goalsAgainst);
                dto.setGoalDifference(goalsFor - goalsAgainst);

            } else {
                dto.setGoalsFor(0);
                dto.setGoalsAgainst(0);
                dto.setGoalDifference(0);
            }

            if (s.getTeam() != null) {
                TeamDTO teamDTO = new TeamDTO();
                teamDTO.setId(s.getTeam().getId());
                teamDTO.setName(s.getTeam().getName());
                teamDTO.setLogo(s.getTeam().getLogo());

                dto.setTeam(teamDTO);
            }

            return dto;

        }).collect(Collectors.toList());

        StandingsResponseDTO response = new StandingsResponseDTO();
        response.setLeagueId(leagueData.getId());
        response.setLeagueName(leagueData.getName());
        response.setCountry(leagueData.getCountry());
        response.setSeason(leagueData.getSeason());
        response.setStandings(result);

        return response;
    }
}