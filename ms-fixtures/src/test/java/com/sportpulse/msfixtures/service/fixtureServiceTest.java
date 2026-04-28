package com.sportpulse.msfixtures.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sportpulse.msfixtures.client.ApiFootballClient;
import com.sportpulse.msfixtures.client.TeamsClient;
import com.sportpulse.msfixtures.dto.FixtureResponseDto;
import com.sportpulse.msfixtures.mapper.FixtureMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class fixtureServiceTest {

    @Mock
    private ApiFootballClient apiFootballClient;

    @Mock
    private TeamsClient teamsClient;

    @Mock
    private FixtureMapper fixtureMapper;

    @InjectMocks
    private FixtureService fixtureService;

    @Test
    void getFixtures_conRespuestaVacia_devuelveListaVacia() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.set("response", mapper.createArrayNode());

        when(apiFootballClient.getFixtures(any(), any(), any(), any())).thenReturn(root);

        List<FixtureResponseDto> result = fixtureService.getFixtures(null, null, null, null);

        assertTrue(result.isEmpty());
    }

    @Test
    void getFixtures_sinFiltros_usaFechaDeHoy() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.set("response", mapper.createArrayNode());

        when(apiFootballClient.getFixtures(isNull(), isNull(), anyString(), isNull()))
                .thenReturn(root);

        List<FixtureResponseDto> result = fixtureService.getFixtures(null, null, null, null);

        verify(apiFootballClient).getFixtures(isNull(), isNull(), anyString(), isNull());
        assertNotNull(result);
    }

    @Test
    void getFixtures_conFiltroLeague_llamaApiConParametro() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.set("response", mapper.createArrayNode());

        when(apiFootballClient.getFixtures(140, null, null, null)).thenReturn(root);

        List<FixtureResponseDto> result = fixtureService.getFixtures(140, null, null, null);

        verify(apiFootballClient).getFixtures(140, null, null, null);
        assertNotNull(result);
    }
}
