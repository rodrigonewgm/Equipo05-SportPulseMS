package com.sportpulse.msfixtures.controller;

import com.sportpulse.msfixtures.dto.FixtureEventDto;
import com.sportpulse.msfixtures.dto.FixtureResponseDto;
import com.sportpulse.msfixtures.service.FixtureService;
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
@RequestMapping("/api/fixtures")
@RequiredArgsConstructor
@Tag(name = "Fixtures", description = "Partidos de fútbol")
public class FixtureController {

    private final FixtureService fixtureService;

    @GetMapping
    @Operation(summary = "Listar partidos", description = "Devuelve partidos filtrables por liga, equipo, fecha o estado. Sin filtros devuelve los del día actual",
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de partidos"),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido o ausente")
    })
    public ResponseEntity<List<FixtureResponseDto>> getFixtures(
            @RequestParam(required = false) Integer league,
            @RequestParam(required = false) Integer team,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String status) {

        return ResponseEntity.ok(fixtureService.getFixtures(league, team, date, status));
    }

    @GetMapping("/live")
    @Operation(summary = "Partidos en curso", description = "Devuelve todos los partidos que se están jugando en este momento",
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponse(responseCode = "200", description = "Lista de partidos en curso")
    public ResponseEntity<List<FixtureResponseDto>> getLiveFixtures() {
        return ResponseEntity.ok(fixtureService.getLiveFixtures());
    }

    @GetMapping("/{fixtureId}/events")
    @Operation(summary = "Eventos de un partido", description = "Devuelve goles, tarjetas y sustituciones de un partido",
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de eventos"),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido o ausente"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado")
    })
    public ResponseEntity<List<FixtureEventDto>> getFixtureEvents(
            @PathVariable Integer fixtureId) {
        return ResponseEntity.ok(fixtureService.getFixtureEvents(fixtureId));
    }
}
