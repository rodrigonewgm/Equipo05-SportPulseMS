package com.sportpulse.msstandings.client;

import com.sportpulse.msstandings.config.FeignConfig;
import com.sportpulse.msstandings.external.ApiFootballStandingsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "api-fotball",
        url = "https://v3.football.api-sports.io",
        configuration = FeignConfig.class
)
public interface ApiFootballClient {
    @GetMapping("/standings")
    ApiFootballStandingsResponse getStandings(
            @RequestParam Integer league,
            @RequestParam Integer season
    );
}
