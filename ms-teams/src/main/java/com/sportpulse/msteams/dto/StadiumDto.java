package com.sportpulse.msteams.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StadiumDto {
    private String name;
    private String city;
    private Integer capacity;
}
