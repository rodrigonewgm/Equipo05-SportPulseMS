package com.sportpulse.msfixtures.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sportpulse.msfixtures.client.ApiFootballClient;
import com.sportpulse.msfixtures.client.TeamsClient;
import com.sportpulse.msfixtures.dto.FixtureEventDto;
import com.sportpulse.msfixtures.exception.FixtureNotFoundException;
import com.sportpulse.msfixtures.mapper.FixtureMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FixtureEventsServiceTest {

    @Mock
    private ApiFootballClient apiFootballClient;

    @Mock
    private TeamsClient teamsClient;

    @Mock
    private FixtureMapper fixtureMapper;

    @InjectMocks
    private FixtureService fixtureService;

    @Test
    void getFixtureEvents_conIdInexistente_lanzaFixtureNotFoundException() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.set("response", mapper.createArrayNode());

        when(apiFootballClient.getFixtureById(9999)).thenReturn(root);

        assertThrows(FixtureNotFoundException.class,
                () -> fixtureService.getFixtureEvents(9999));
    }

    @Test
    void getFixtureEvents_conRespuestaVacia_devuelveListaVacia() {
        ObjectMapper mapper = new ObjectMapper();

        // fixture existe
        ObjectNode fixtureCheck = mapper.createObjectNode();
        ArrayNode fixtureArray = mapper.createArrayNode();
        fixtureArray.add(mapper.createObjectNode());
        fixtureCheck.set("response", fixtureArray);

        // eventos vacíos
        ObjectNode eventsRoot = mapper.createObjectNode();
        eventsRoot.set("response", mapper.createArrayNode());

        when(apiFootballClient.getFixtureById(1)).thenReturn(fixtureCheck);
        when(apiFootballClient.getFixtureEvents(1)).thenReturn(eventsRoot);

        List<FixtureEventDto> result = fixtureService.getFixtureEvents(1);

        assertTrue(result.isEmpty());
    }

    @Test
    void getLiveFixtures_devuelveListaVacia_cuandoNoHayPartidos() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.set("response", mapper.createArrayNode());

        when(apiFootballClient.getLiveFixtures()).thenReturn(root);

        var result = fixtureService.getLiveFixtures();

        assertTrue(result.isEmpty());
    }
}
