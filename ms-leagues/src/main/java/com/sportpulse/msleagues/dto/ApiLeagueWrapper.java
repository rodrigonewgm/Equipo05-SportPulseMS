package com.sportpulse.msleagues.dto;

import lombok.*;

import java.util.List;

// representa una liga dentro del array "response" de la Api
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiLeagueWrapper {

    private ApiLeague league;
    private ApiCountry country;
    private List<ApiSeason> seasons;
}
