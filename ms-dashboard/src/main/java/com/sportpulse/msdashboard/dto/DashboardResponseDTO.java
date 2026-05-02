package com.sportpulse.msdashboard.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class DashboardResponseDTO {
    private LeagueDTO league;

    private String today;

    private List<MatchDTO> matchesToday;

    private List<StandingPreviewDTO> standingsPreview;

    private List<TopScorerDTO> topScorers;

    private List<String> errors;

    private Instant lastUpdated;
}
