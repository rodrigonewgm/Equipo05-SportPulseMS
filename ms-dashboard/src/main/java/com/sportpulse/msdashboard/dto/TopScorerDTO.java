package com.sportpulse.msdashboard.dto;

import lombok.Data;

@Data
public class TopScorerDTO {
    private Integer rank;
    private String name;
    private String team;
    private Integer goals;
}
