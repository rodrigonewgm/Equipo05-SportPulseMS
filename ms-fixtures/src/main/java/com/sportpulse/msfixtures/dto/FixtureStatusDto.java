package com.sportpulse.msfixtures.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixtureStatusDto {
    private String shortStatus;
    private String longStatus;
}
