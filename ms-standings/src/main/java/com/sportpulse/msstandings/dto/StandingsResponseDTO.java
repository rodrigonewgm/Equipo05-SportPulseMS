package com.sportpulse.msstandings.dto;

import lombok.Data;

import java.util.List;

@Data
public class StandingsResponseDTO {

    private Integer leagueId;
    private String leagueName;
    private String country;
    private Integer season;

    private List<StandingDTO> standings;
}
