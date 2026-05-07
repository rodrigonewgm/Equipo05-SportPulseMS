package com.sportpulse.msteams.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sportpulse.msteams.client.ApiFootballClient;
import com.sportpulse.msteams.dto.StadiumDetailDto;
import com.sportpulse.msteams.dto.TeamDetailResponseDto;
import com.sportpulse.msteams.exception.TeamNotFoundException;
import com.sportpulse.msteams.mapper.TeamDetailMapper;
import com.sportpulse.msteams.mapper.TeamMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamDetailServiceTest {

        @Mock
        private ApiFootballClient apiFootballClient;

        @Mock
        private TeamMapper teamMapper;

        @Mock
        private TeamDetailMapper teamDetailMapper;

        @InjectMocks
        private TeamService teamService;

        @Test
        void getTeamById_devuelveDetalle() {
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
            team.put("national", false);

            ObjectNode venue = mapper.createObjectNode();
            venue.put("name", "Camp Nou");
            venue.put("address", "C/ Arístides Maillol");
            venue.put("city", "Barcelona");
            venue.put("capacity", 99354);
            venue.put("surface", "grass");

            item.set("team", team);
            item.set("venue", venue);
            responseArray.add(item);
            root.set("response", responseArray);

            TeamDetailResponseDto expectedDto = TeamDetailResponseDto.builder()
                    .id(529)
                    .name("FC Barcelona")
                    .country("Spain")
                    .logo("https://logo.png")
                    .founded(1899)
                    .national(false)
                    .stadium(StadiumDetailDto.builder()
                            .name("Camp Nou")
                            .city("Barcelona")
                            .capacity(99354)
                            .surface("grass")
                            .build())
                    .build();

            when(apiFootballClient.getTeamById(529)).thenReturn(root);
            when(teamDetailMapper.toDto(anyInt(), anyString(), anyString(), anyString(),
                    anyInt(), any(Boolean.class), any(StadiumDetailDto.class)))
                    .thenReturn(expectedDto);

            TeamDetailResponseDto result = teamService.getTeamById(529);

            assertEquals("FC Barcelona", result.getName());
            assertEquals("Camp Nou", result.getStadium().getName());
            assertEquals("grass", result.getStadium().getSurface());
        }

        @Test
        void getTeamById_conIdInexistente_lanzaTeamNotFoundException() {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            root.set("response", mapper.createArrayNode());

            when(apiFootballClient.getTeamById(9999)).thenReturn(root);

            assertThrows(TeamNotFoundException.class, () -> teamService.getTeamById(9999));
        }

        @Test
        void getTeamById_conRespuestaNull_lanzaTeamNotFoundException() {
            when(apiFootballClient.getTeamById(9999)).thenReturn(null);

            assertThrows(TeamNotFoundException.class, () -> teamService.getTeamById(9999));
        }
}
