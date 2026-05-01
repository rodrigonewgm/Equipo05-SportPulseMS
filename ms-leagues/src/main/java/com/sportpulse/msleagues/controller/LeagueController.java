package com.sportpulse.msleagues.controller;

import com.sportpulse.msleagues.dto.LeagueDetailResponseDto;
import com.sportpulse.msleagues.dto.LeagueResponseDto;
import com.sportpulse.msleagues.service.LeagueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leagues")
@RequiredArgsConstructor
@Tag(name = "Leagues", description = "Ligas de fútbol disponibles")
public class LeagueController {

    private final LeagueService leagueService;

    @GetMapping
    @Operation(summary = "Listar ligas", description = "Devuelve ligas filtrables por país o temporada",
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de ligas"),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido o ausente")
    })
    public ResponseEntity<List<LeagueResponseDto>> getLeagues(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer season) {
        return ResponseEntity.ok(leagueService.getLeagues(country, season));
    }

    @GetMapping("/{leagueId}")
    public ResponseEntity<LeagueDetailResponseDto> getLeagueDetail(
            @PathVariable Integer leagueId,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(leagueService.getLeagueDetail(leagueId, authHeader));
    }
}
