package com.sportpulse.msfixtures.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixtureLeagueDto {
    private Integer id;
    private String name;
    private String round;
}
