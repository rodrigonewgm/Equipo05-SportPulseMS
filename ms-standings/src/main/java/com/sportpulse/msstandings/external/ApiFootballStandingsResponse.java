package com.sportpulse.msstandings.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApiFootballStandingsResponse {

    private List<Response> response;

    @Data
    public static class Response {
        private League league;
    }

    @Data
    public static class League {
        private int id;
        private String name;
        private String country;
        private int season;
        private List<List<Standing>> standings;
    }

    @Data
    public static class Standing {
        private int rank;
        private Team team;
        private int points;
        private int played;
        private int won;
        private int draw;
        private int lose;
        private Goals goals;
        private String form;
        private All all;
    }

    @Data
    public static class Team {
        private int id;
        private String name;
        private String logo;
    }

    @Data
    public static class Goals {
        @JsonProperty("for")
        private int forGoals;
        private int against;
    }

    @Data
    public static class All {
        private int played;
        private int win;
        private int draw;
        private int lose;
        private Goals goals;
    }
}
