package com.sportpulse.msleagues.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sportpulse.msleagues.client.ApiFootballClient;
import com.sportpulse.msleagues.dto.LeagueResponseDto;
import com.sportpulse.msleagues.dto.ApiFootballResponse;
import com.sportpulse.msleagues.dto.ApiLeagueWrapper;
import com.sportpulse.msleagues.dto.LeagueDetailResponseDto;
import com.sportpulse.msleagues.exception.LeagueNotFoundException;
import com.sportpulse.msleagues.mapper.LeagueMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeagueServiceTest {

    @Mock
    private ApiFootballClient apiFootballClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private LeagueMapper leagueMapper;

    @InjectMocks
    private LeagueService leagueService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(leagueService, "authServiceUrl", "http://localhost:8081/api/auth/validate");
        ReflectionTestUtils.setField(leagueService, "baseUrl", "http://fake-api");
        ReflectionTestUtils.setField(leagueService, "apiKey", "fake-key");
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

    @Test
    void shouldReturnLeagueDetail_whenApiReturnsData() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("valid"));

        ApiLeagueWrapper wrapper = new ApiLeagueWrapper();
        ApiFootballResponse apiResp = new ApiFootballResponse(List.of(wrapper));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ApiFootballResponse.class)))
                .thenReturn(ResponseEntity.ok(apiResp));

        LeagueDetailResponseDto dto = new LeagueDetailResponseDto();
        when(leagueMapper.toDetailDto(any())).thenReturn(dto);

        LeagueDetailResponseDto result = leagueService.getLeagueDetail(140, "Bearer token");
        assertNotNull(result);
    }

    @Test
    void shouldThrowException_whenLeagueNotFound() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("valid"));

        ApiFootballResponse emptyResponse = new ApiFootballResponse(List.of());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ApiFootballResponse.class)))
                .thenReturn(ResponseEntity.ok(emptyResponse));

        assertThrows(LeagueNotFoundException.class, () ->
                leagueService.getLeagueDetail(140, "Bearer token"));
    }

    @Test
    void shouldUseCache_onSecondCall() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("valid"));

        ApiLeagueWrapper wrapper = new ApiLeagueWrapper();
        ApiFootballResponse apiResp = new ApiFootballResponse(List.of(wrapper));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ApiFootballResponse.class)))
                .thenReturn(ResponseEntity.ok(apiResp));

        when(leagueMapper.toDetailDto(any())).thenReturn(new LeagueDetailResponseDto());

        leagueService.getLeagueDetail(140, "Bearer token");
        leagueService.getLeagueDetail(140, "Bearer token");

        verify(restTemplate, times(1))
                .exchange(anyString(), eq(HttpMethod.GET), any(), eq(ApiFootballResponse.class));
    }
}
