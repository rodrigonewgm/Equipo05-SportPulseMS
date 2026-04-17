package com.sportpulse.msauth.exception;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException() {
        super("Email o contraseña incorrectos");
    }
}
