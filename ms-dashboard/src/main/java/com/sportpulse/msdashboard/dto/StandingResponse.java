package com.sportpulse.msdashboard.dto;

import lombok.Data;

import java.util.List;

@Data
public class StandingResponse {

    private List<List<StandingPreviewDTO>> response;
}