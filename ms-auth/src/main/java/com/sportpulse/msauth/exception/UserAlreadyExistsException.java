package com.sportpulse.msauth.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(){
        super("USER_ALREADY_EXISTS");
    }
}
