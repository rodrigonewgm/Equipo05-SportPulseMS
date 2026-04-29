package com.sportpulse.msfixtures.exception;

public class FixtureNotFoundException extends RuntimeException{
    public FixtureNotFoundException(Integer fixtureId) {
        super("No existe un partido con el ID: " + fixtureId);
    }
}
