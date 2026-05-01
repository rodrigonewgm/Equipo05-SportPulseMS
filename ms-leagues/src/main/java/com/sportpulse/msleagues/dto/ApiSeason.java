package com.sportpulse.msleagues.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiSeason {
    private Integer year;
    private String start;
    private String end;
    private Boolean current;
}
