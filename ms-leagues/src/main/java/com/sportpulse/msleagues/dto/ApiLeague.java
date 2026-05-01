package com.sportpulse.msleagues.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiLeague {
    private Integer id;
    private String name;
    private String type;
    private String logo;
}
