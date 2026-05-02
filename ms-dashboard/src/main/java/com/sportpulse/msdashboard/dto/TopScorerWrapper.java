package com.sportpulse.msdashboard.dto;

import lombok.Data;

import java.util.List;

@Data
public class TopScorerWrapper {
    private PlayerDTO player;
    private List<StatisticDTO> statistics;
}
