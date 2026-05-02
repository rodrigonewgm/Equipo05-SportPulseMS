package com.sportpulse.msdashboard.controller;

import com.sportpulse.msdashboard.client.AuthClient;
import com.sportpulse.msdashboard.dto.DashboardResponseDTO;
import com.sportpulse.msdashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    private final AuthClient authClient;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboard(
            @RequestParam Integer league,
            @RequestParam Integer season,
            @RequestHeader("Authorization") String token
    ) {

        try {
            authClient.validate(token);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Token inválido o expirado"
            );
        }

        DashboardResponseDTO response =
                dashboardService.getDashboard(league, season, token);

        return ResponseEntity.ok(response);
    }
}
