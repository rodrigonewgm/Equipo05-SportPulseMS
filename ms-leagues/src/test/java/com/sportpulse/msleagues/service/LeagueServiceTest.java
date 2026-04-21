package com.sportpulse.msleagues.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sportpulse.msleagues.client.ApiFootballClient;
import com.sportpulse.msleagues.dto.LeagueResponseDto;
import com.sportpulse.msleagues.mapper.LeagueMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeagueServiceTest {

    @Mock
    private ApiFootballClient apiFootballClient;

    @InjectMocks
    private LeagueService leagueService;

    @Mock
    private LeagueMapper leagueMapper;

    @Test
    void getLeagues_sinFiltros_devuelveLista() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        ArrayNode responseArray = mapper.createArrayNode();

        ObjectNode item = mapper.createObjectNode();
        ObjectNode league = mapper.createObjectNode();
        league.put("id", 140);
        league.put("name", "La Liga");
        league.put("type", "League");
        league.put("logo", "https://logo.png");

        ObjectNode country = mapper.createObjectNode();
        country.put("name", "Spain");

        ObjectNode season = mapper.createObjectNode();
        season.put("year", 2024);
        season.put("current", true);
        season.put("start", "2024-08-17");
        season.put("end", "2025-05-25");
        ArrayNode seasonsArray = mapper.createArrayNode();
        seasonsArray.add(season);

        item.set("league", league);
        item.set("country", country);
        item.set("seasons", seasonsArray);
        responseArray.add(item);
        root.set("response", responseArray);

        when(apiFootballClient.getLeagues(null, null)).thenReturn(root);

        List<LeagueResponseDto> result = leagueService.getLeagues(null, null);

        assertEquals(1, result.size());
        assertEquals("La Liga", result.get(0).getName());
        assertEquals("Spain", result.get(0).getCountry());
        assertEquals(2024, result.get(0).getCurrentSeason());
    }

    @Test
    void getLeagues_conFiltroCountry_llamaApiConParametro() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.set("response", mapper.createArrayNode());

        when(apiFootballClient.getLeagues("Spain", null)).thenReturn(root);

        List<LeagueResponseDto> result = leagueService.getLeagues("Spain", null);

        verify(apiFootballClient).getLeagues("Spain", null);
        assertNotNull(result);
    }

    @Test
    void getLeagues_conRespuestaVacia_devuelveListaVacia() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.set("response", mapper.createArrayNode());

        when(apiFootballClient.getLeagues(null, null)).thenReturn(root);

        List<LeagueResponseDto> result = leagueService.getLeagues(null, null);

        assertTrue(result.isEmpty());
    }
}
