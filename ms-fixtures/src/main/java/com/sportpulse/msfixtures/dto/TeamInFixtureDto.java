package com.sportpulse.msfixtures.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamInFixtureDto {
    private Integer id;
    private String name;
    private String logo;
    private Integer goals;
}
