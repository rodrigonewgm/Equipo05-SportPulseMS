package com.sportpulse.msdashboard.dto;

import lombok.Data;

@Data
public class StandingPreviewDTO {
    private Integer rank;
    private String team;
    private Integer points;
    private Integer played;
}
