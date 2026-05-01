package com.sportpulse.msleagues.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiFootballResponse {
    private List<ApiLeagueWrapper> response;
}
