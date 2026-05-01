package com.sportpulse.msstandings.controller;

import com.sportpulse.msstandings.client.AuthClient;
import com.sportpulse.msstandings.dto.StandingDTO;
import com.sportpulse.msstandings.dto.StandingsResponseDTO;
import com.sportpulse.msstandings.service.StandingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/standings")
@RequiredArgsConstructor
@Tag(name = "Standings", description = "Clasificaciones de ligas de fútbol")
public class StandingController {

    private final StandingsService standingsService;
    private final AuthClient authClient;

    @GetMapping
    @Operation(summary = "Clasificación completa", description = "Devuelve la tabla de clasificación de una liga y temporada",
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clasificación obtenida"),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido o ausente"),
            @ApiResponse(responseCode = "400", description = "Parámetros requeridos faltantes")
    })
    public ResponseEntity<StandingsResponseDTO> getStandings(
            @RequestParam Integer league,
            @RequestParam Integer season,
            @RequestHeader("Authorization") String token
    ) {
        authClient.validate(token);
        StandingsResponseDTO response = standingsService.getStandings(league, season, token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/team/{teamId}")
    @Operation(summary = "Posición de un equipo", description = "Devuelve la posición específica de un equipo en la clasificación",
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Posición obtenida"),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido o ausente"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado en esa liga o temporada")
    })
    public ResponseEntity<StandingDTO> getTeamStanding(
            @PathVariable Integer teamId,
            @RequestParam Integer league,
            @RequestParam Integer season,
            @RequestHeader("Authorization") String token
    ) {

        authClient.validate(token);

        StandingDTO response = standingsService.getTeamStanding(teamId, league, season);

        return ResponseEntity.ok(response);
    }
}