package com.sportpulse.msleagues.exception;

public class LeagueNotFoundException extends RuntimeException{

    public LeagueNotFoundException(){
        super("No existe una liga con el ID proporcionado");
    }
}
