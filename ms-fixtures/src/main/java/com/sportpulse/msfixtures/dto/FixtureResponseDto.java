package com.sportpulse.msfixtures.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixtureResponseDto {
    private Integer id;
    private String date;
    private FixtureStatusDto status;
    private FixtureLeagueDto league;
    private TeamInFixtureDto homeTeam;
    private TeamInFixtureDto awayTeam;
    private VenueDto venue;
}

