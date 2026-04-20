package com.sportpulse.msleagues.controller;

import com.sportpulse.msleagues.dto.LeagueResponseDto;
import com.sportpulse.msleagues.service.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leagues")
@RequiredArgsConstructor

public class leagueController {

    private final LeagueService leagueService;

    @GetMapping
    public ResponseEntity<List<LeagueResponseDto>> getLeagues(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer season) {

        return ResponseEntity.ok(leagueService.getLeagues(country, season));
    }
}
