package com.sportpulse.msfixtures.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixtureEventDto {
    private Integer elapsed;
    private String type;
    private String detail;
    private TeamInFixtureDto team;
    private PlayerDto player;
    private PlayerDto assist;
}
