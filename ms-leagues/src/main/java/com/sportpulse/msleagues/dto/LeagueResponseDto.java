package com.sportpulse.msleagues.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LeagueResponseDto {

    private Integer id;
    private String name;
    private String type;
    private String country;
    private String logo;
    private Integer currentSeason;
    private String startDate;
    private String endDate;
}
