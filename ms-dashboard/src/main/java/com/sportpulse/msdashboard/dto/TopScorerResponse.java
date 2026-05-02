package com.sportpulse.msdashboard.dto;

import lombok.Data;

import java.util.List;

@Data
public class TopScorerResponse {
    private List<TopScorerWrapper> response;
}
