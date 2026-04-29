package com.sportpulse.msfixtures.controller;

import com.sportpulse.msfixtures.dto.FixtureEventDto;
import com.sportpulse.msfixtures.dto.FixtureResponseDto;
import com.sportpulse.msfixtures.service.FixtureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fixtures")
@RequiredArgsConstructor
public class FixtureController {

    private final FixtureService fixtureService;

    @GetMapping
    public ResponseEntity<List<FixtureResponseDto>> getFixtures(
            @RequestParam(required = false) Integer league,
            @RequestParam(required = false) Integer team,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String status) {

        return ResponseEntity.ok(fixtureService.getFixtures(league, team, date, status));
    }

    @GetMapping("/live")
    public ResponseEntity<List<FixtureResponseDto>> getLiveFixtures() {
        return ResponseEntity.ok(fixtureService.getLiveFixtures());
    }

    @GetMapping("/{fixtureId}/events")
    public ResponseEntity<List<FixtureEventDto>> getFixtureEvents(
            @PathVariable Integer fixtureId) {
        return ResponseEntity.ok(fixtureService.getFixtureEvents(fixtureId));
    }
}
