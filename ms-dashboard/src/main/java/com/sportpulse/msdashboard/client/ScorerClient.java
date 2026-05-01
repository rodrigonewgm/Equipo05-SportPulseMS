package com.sportpulse.msdashboard.client;

import com.sportpulse.msdashboard.dto.TopScorerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "api-football", url = "${rapidapi.base-url}")
public interface ScorerClient {

    @GetMapping("/players/topscorers")
    TopScorerResponse getTopScorers(
            @RequestHeader("X-apisports-key") String apiKey,
            @RequestParam Integer league,
            @RequestParam Integer season
    );
}