package com.sportpulse.msdashboard.dto;

import lombok.Data;

@Data
public class MatchDTO {
    private Integer id;
    private String date;
    private TeamDTO homeTeam;
    private TeamDTO awayTeam;
    private String status;
}
