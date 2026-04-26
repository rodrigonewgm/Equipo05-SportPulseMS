package com.sportpulse.msteams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sportpulse.msteams.client.ApiFootballClient;
import com.sportpulse.msteams.dto.StadiumDto;
import com.sportpulse.msteams.dto.TeamResponseDto;
import com.sportpulse.msteams.mapper.TeamMapper;
import com.sportpulse.msteams.service.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private ApiFootballClient apiFootballClient;

    @Mock
    private TeamMapper teamMapper;

    @InjectMocks
    private TeamService teamService;

    @Test
    void getTeams_devuelveLista() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        ArrayNode responseArray = mapper.createArrayNode();

        ObjectNode item = mapper.createObjectNode();

        ObjectNode team = mapper.createObjectNode();
        team.put("id", 529);
        team.put("name", "FC Barcelona");
        team.put("country", "Spain");
        team.put("logo", "https://logo.png");
        team.put("founded", 1899);

        ObjectNode venue = mapper.createObjectNode();
        venue.put("name", "Camp Nou");
        venue.put("city", "Barcelona");
        venue.put("capacity", 99354);

        item.set("team", team);
        item.set("venue", venue);
        responseArray.add(item);
        root.set("response", responseArray);

        StadiumDto stadiumDto = StadiumDto.builder()
                .name("Camp Nou")
                .city("Barcelona")
                .capacity(99354)
                .build();

        TeamResponseDto expectedDto = TeamResponseDto.builder()
                .id(529)
                .name("FC Barcelona")
                .country("Spain")
                .logo("https://logo.png")
                .founded(1899)
                .stadium(stadiumDto)
                .build();

        when(apiFootballClient.getTeams(140, 2024)).thenReturn(root);
        when(teamMapper.toDto(529, "FC Barcelona", "Spain", "https://logo.png",
                1899, any(StadiumDto.class))).thenReturn(expectedDto);

        List<TeamResponseDto> result = teamService.getTeams(140, 2024);

        assertEquals(1, result.size());
        assertEquals("FC Barcelona", result.get(0).getName());
        assertEquals("Spain", result.get(0).getCountry());
    }

    @Test
    void getTeams_conRespuestaVacia_devuelveListaVacia() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.set("response", mapper.createArrayNode());

        when(apiFootballClient.getTeams(140, 2024)).thenReturn(root);

        List<TeamResponseDto> result = teamService.getTeams(140, 2024);

        assertTrue(result.isEmpty());
    }

    @Test
    void getTeams_llamaApiConParametrosCorrectos() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.set("response", mapper.createArrayNode());

        when(apiFootballClient.getTeams(39, 2024)).thenReturn(root);

        teamService.getTeams(39, 2024);

        verify(apiFootballClient).getTeams(39, 2024);
    }
}
