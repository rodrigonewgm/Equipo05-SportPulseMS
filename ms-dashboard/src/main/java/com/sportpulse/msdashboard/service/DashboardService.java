package com.sportpulse.msdashboard.service;

import com.sportpulse.msdashboard.client.FixtureClient;
import com.sportpulse.msdashboard.client.ScorerClient;
import com.sportpulse.msdashboard.client.StandingsClient;
import com.sportpulse.msdashboard.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final FixtureClient fixtureClient;
    private final StandingsClient standingsClient;
    private final ScorerClient scorerClient;

    @Value("${rapidapi.key}")
    private String apiKey;

    public DashboardResponseDTO getDashboard(Integer league, Integer season, String token) {

        DashboardResponseDTO response = new DashboardResponseDTO();
        List<String> errors = new ArrayList<>();

        response.setToday(LocalDate.now().toString());

        try {
            List<MatchDTO> matches = fixtureClient.getFixturesByDate(
                    token,
                    league,
                    LocalDate.now().toString()
            );
            response.setMatchesToday(matches);

        } catch (Exception e) {
            e.printStackTrace();
            errors.add("fixtures unavailable");
            response.setMatchesToday(List.of());
        }

        // 🔥 STANDINGS
        try {
            StandingResponse standings = standingsClient.getStandings(
                    token,
                    league,
                    season
            );

            if (standings.getResponse() != null && !standings.getResponse().isEmpty()) {

                List<StandingPreviewDTO> top3 =
                        standings.getResponse().get(0).stream()
                                .limit(3)
                                .toList();

                response.setStandingsPreview(top3);

            } else {
                errors.add("standings unavailable");
                response.setStandingsPreview(List.of());
            }

        } catch (Exception e) {
            e.printStackTrace();
            errors.add("standings unavailable");
            response.setStandingsPreview(List.of());
        }

        // 🔥 SCORERS
        try {
            TopScorerResponse scorerResponse =
                    scorerClient.getTopScorers(apiKey, league, season);

            List<TopScorerDTO> top3 = new ArrayList<>();
            int rank = 1;

            if (scorerResponse.getResponse() != null) {

                for (TopScorerWrapper w : scorerResponse.getResponse()) {

                    if (rank > 3) break;

                    TopScorerDTO dto = new TopScorerDTO();
                    dto.setRank(rank);
                    dto.setName(w.getPlayer().getName());
                    StatisticDTO stat = w.getStatistics().get(0);

                    dto.setTeam(stat.getTeam().getName());
                    dto.setGoals(stat.getGoals().getTotal());

                    top3.add(dto);
                    rank++;
                }

                response.setTopScorers(top3);

            } else {
                errors.add("scorers unavailable");
                response.setTopScorers(List.of());
            }

        } catch (Exception e) {
            e.printStackTrace();
            errors.add("scorers unavailable");
            response.setTopScorers(List.of());
        }

        response.setErrors(errors);
        response.setLastUpdated(Instant.now());

        return response;
    }
}
