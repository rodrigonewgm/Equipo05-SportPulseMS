package com.sportpulse.msteams.controller;

import com.sportpulse.msteams.dto.TeamDetailResponseDto;
import com.sportpulse.msteams.dto.TeamResponseDto;
import com.sportpulse.msteams.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "Teams", description = "Equipos de fútbol")
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    @Operation(summary = "Listar equipos", description = "Devuelve equipos de una liga y temporada específica",
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de equipos"),
            @ApiResponse(responseCode = "400", description = "Parámetros requeridos faltantes"),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido o ausente")
    })
    public ResponseEntity<List<TeamResponseDto>> getTeams(
            @RequestParam @NotNull Integer league,
            @RequestParam @NotNull Integer season) {

        return ResponseEntity.ok(teamService.getTeams(league, season));
    }

    @GetMapping("/{teamId}")
    @Operation(summary = "Detalle de equipo", description = "Devuelve información completa de un equipo por ID",
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle del equipo"),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido o ausente"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    public ResponseEntity<TeamDetailResponseDto> getTeamById(@PathVariable Integer teamId) {
        return ResponseEntity.ok(teamService.getTeamById(teamId));
    }
}
