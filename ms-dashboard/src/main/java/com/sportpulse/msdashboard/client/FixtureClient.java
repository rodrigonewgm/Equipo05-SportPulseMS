package com.sportpulse.msdashboard.client;

import com.sportpulse.msdashboard.dto.MatchDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-fixtures", url = "${services.fixtures-url}")
public interface FixtureClient {

    @GetMapping("/api/fixtures")
    List<MatchDTO> getFixturesByDate(
            @RequestHeader("Authorization") String token,
            @RequestParam Integer league,
            @RequestParam String date
    );
}