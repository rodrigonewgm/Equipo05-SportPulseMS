package com.sportpulse.msstandings.dto;

import lombok.Data;

@Data
public class StandingDTO {

    private Integer rank;

    private TeamDTO team;

    private Integer points;

    private Integer played;

    private Integer won;

    private Integer drawn;

    private Integer lost;

    private Integer goalsFor;

    private Integer goalsAgainst;

    private Integer goalDifference;

    private String form;

    private String description;
}
