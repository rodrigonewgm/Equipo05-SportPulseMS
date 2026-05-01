package com.sportpulse.msdashboard.service;

import com.sportpulse.msdashboard.client.FixtureClient;
import com.sportpulse.msdashboard.client.ScorerClient;
import com.sportpulse.msdashboard.client.StandingsClient;
import com.sportpulse.msdashboard.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DashboardServiceTest {

    private FixtureClient fixtureClient;
    private StandingsClient standingsClient;
    private ScorerClient scorerClient;

    private DashboardService service;

    @BeforeEach
    void setUp() {
        fixtureClient = mock(FixtureClient.class);
        standingsClient = mock(StandingsClient.class);
        scorerClient = mock(ScorerClient.class);

        service = new DashboardService(fixtureClient, standingsClient, scorerClient);

        service = Mockito.spy(service);
        doReturn("test-key").when(service).getClass(); // hack simple (no rompe test)
    }

    @Test
    void shouldReturnFullDashboard_whenAllServicesOk() {

        when(fixtureClient.getFixturesByDate(anyString(), anyInt(), anyString()))
                .thenReturn(List.of(new MatchDTO()));

        StandingResponse standings = new StandingResponse();
        standings.setResponse(List.of(List.of(new StandingPreviewDTO())));
        when(standingsClient.getStandings(anyString(), anyInt(), anyInt()))
                .thenReturn(standings);

        TopScorerWrapper wrapper = new TopScorerWrapper();
        PlayerDTO player = new PlayerDTO();
        player.setName("Messi");
        wrapper.setPlayer(player);

        TeamDTO team = new TeamDTO();
        team.setName("Inter Miami");

        GoalsDTO goals = new GoalsDTO();
        goals.setTotal(10);

        StatisticDTO stat = new StatisticDTO();
        stat.setTeam(team);
        stat.setGoals(goals);
        wrapper.setStatistics(List.of(stat));

        TopScorerResponse scorerResponse = new TopScorerResponse();
        scorerResponse.setResponse(List.of(wrapper));

        when(scorerClient.getTopScorers(anyString(), anyInt(), anyInt()))
                .thenReturn(scorerResponse);

        DashboardResponseDTO result = service.getDashboard(140, 2024, "token");

        assertFalse(result.getMatchesToday().isEmpty());
        assertFalse(result.getStandingsPreview().isEmpty());
        assertFalse(result.getTopScorers().isEmpty());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void shouldReturnPartial_whenFixturesFail() {

        when(fixtureClient.getFixturesByDate(anyString(), anyInt(), anyString()))
                .thenThrow(new RuntimeException());

        when(standingsClient.getStandings(anyString(), anyInt(), anyInt()))
                .thenReturn(new StandingResponse());

        when(scorerClient.getTopScorers(anyString(), anyInt(), anyInt()))
                .thenReturn(new TopScorerResponse());

        DashboardResponseDTO result = service.getDashboard(140, 2024, "token");

        assertTrue(result.getMatchesToday().isEmpty());
        assertTrue(result.getErrors().contains("fixtures unavailable"));
    }

    @Test
    void shouldReturnErrors_whenAllFail() {

        when(fixtureClient.getFixturesByDate(anyString(), anyInt(), anyString()))
                .thenThrow(new RuntimeException());

        when(standingsClient.getStandings(anyString(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException());

        when(scorerClient.getTopScorers(anyString(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException());

        DashboardResponseDTO result = service.getDashboard(140, 2024, "token");

        assertTrue(result.getMatchesToday().isEmpty());
        assertTrue(result.getStandingsPreview().isEmpty());
        assertTrue(result.getTopScorers().isEmpty());
        assertEquals(3, result.getErrors().size());
    }
}