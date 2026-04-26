package com.sportpulse.msteams.controller;

import com.sportpulse.msteams.dto.TeamResponseDto;
import com.sportpulse.msteams.service.TeamService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<List<TeamResponseDto>> getTeams(
            @RequestParam @NotNull Integer league,
            @RequestParam @NotNull Integer season) {

        return ResponseEntity.ok(teamService.getTeams(league, season));
    }
}
