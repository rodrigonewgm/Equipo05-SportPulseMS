package com.sportpulse.msteams.exception;

public class TeamNotFoundException extends RuntimeException{
    public TeamNotFoundException(Integer teamId) {
        super("No existe un equipo con el ID: " + teamId);
    }
}
