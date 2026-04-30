package com.sportpulse.msleagues.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeagueDetailResponseDto {

    private Integer id;
    private String name;
    private String type;
    private String country;
    private String logo;
    private List<Integer> seasons;
    private CurrentSeasonDto currentSeason;
}
