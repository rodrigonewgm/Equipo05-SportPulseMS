package com.sportpulse.msleagues.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentSeasonDto {
    private Integer year;
    private String startDate;
    private String endDate;
    private Boolean current;
}
