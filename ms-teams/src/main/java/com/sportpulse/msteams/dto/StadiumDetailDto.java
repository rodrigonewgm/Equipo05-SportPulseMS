package com.sportpulse.msteams.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StadiumDetailDto {
    private String name;
    private String address;
    private String city;
    private Integer capacity;
    private String surface;
}
