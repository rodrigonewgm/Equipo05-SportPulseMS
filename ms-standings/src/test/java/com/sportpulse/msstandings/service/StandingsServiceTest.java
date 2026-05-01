package com.sportpulse.msstandings.service;

import com.sportpulse.msstandings.client.ApiFootballClient;
import com.sportpulse.msstandings.external.ApiFootballStandingsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StandingsServiceTest {

    @Mock
    private ApiFootballClient apiFootballClient;

    @InjectMocks
    private StandingsService standingsService;

    @Test
    void testGetStandings_noExplota() {

        ApiFootballStandingsResponse response = new ApiFootballStandingsResponse();

        ApiFootballStandingsResponse.Response res = new ApiFootballStandingsResponse.Response();
        ApiFootballStandingsResponse.League league = new ApiFootballStandingsResponse.League();

        league.setStandings(List.of(List.of()));

        res.setLeague(league);
        response.setResponse(List.of(res));

        when(apiFootballClient.getStandings(140, 2024))
                .thenReturn(response);

        var result = standingsService.getStandings(140, 2024, "token");

        assertNotNull(result);
    }
}