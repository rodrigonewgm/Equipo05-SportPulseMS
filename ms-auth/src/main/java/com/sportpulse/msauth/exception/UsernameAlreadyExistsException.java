package com.sportpulse.msauth.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(){

        super("USERNAME_ALREADY_EXISTS");
    }
}
