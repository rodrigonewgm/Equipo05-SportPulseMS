package com.sportpulse.msteams.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponseDto {
    private Integer id;
    private String name;
    private String country;
    private String logo;
    private Integer founded;
    private StadiumDto stadium;
}
