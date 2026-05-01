package com.sportpulse.msdashboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import com.sportpulse.msdashboard.dto.StandingResponse;

@FeignClient(name = "ms-standings", url = "${services.standings-url}")
public interface StandingsClient {

    @GetMapping("/api/standings")
    StandingResponse getStandings(
            @RequestHeader("Authorization") String token,
            @RequestParam Integer league,
            @RequestParam Integer season
    );
}