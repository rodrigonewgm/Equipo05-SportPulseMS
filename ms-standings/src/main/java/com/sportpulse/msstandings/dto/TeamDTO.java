package com.sportpulse.msstandings.dto;

import lombok.Data;

@Data // getters, setters, toString
public class TeamDTO {
    // id del equipo
    private Integer id;

    // nombre del equipo (viene de ms-teams)
    private String name;

    // logo del equipo (viene de ms-teams)
    private String logo;
}
