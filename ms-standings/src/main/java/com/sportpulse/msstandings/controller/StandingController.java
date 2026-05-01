package com.sportpulse.msstandings.controller;

import com.sportpulse.msstandings.client.AuthClient;
import com.sportpulse.msstandings.dto.StandingDTO;
import com.sportpulse.msstandings.dto.StandingsResponseDTO;
import com.sportpulse.msstandings.service.StandingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/standings")
@RequiredArgsConstructor
public class StandingController {

    private final StandingsService standingsService;
    private final AuthClient authClient;

    @GetMapping
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