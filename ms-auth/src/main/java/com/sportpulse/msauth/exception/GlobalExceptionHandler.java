package com.sportpulse.msauth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Maneja excepcion de mail duplicado
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserExits(){

        Map<String, Object> error = new HashMap<>();

        error.put("error", "USER_ALREADY_EXISTS");
        error.put("message", "Ya existe un usuario con ese email");
        error.put("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    //Maneja excepcion de username duplicado
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameExits(){

        Map<String, Object> error = new HashMap<>();

        error.put("error", "USERNAME_ALREADY_EXISTS");
        error.put("message", "Ya existe un usuario con ese username");
        error.put("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(InvalidCredentialsException ex){

        Map<String, Object> error = new HashMap<>();
        error.put("error", "VALIDATION_ERROR");
        error.put("message", "Email o contraseña incorrectos");
        error.put("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex){

        Map<String, Object> error = new HashMap<>();
        error.put("error", "VALIDATION_ERROR");
        error.put("message", ex.getBindingResult().getFieldError().getDefaultMessage());
        error.put("timestamp", Instant.now());

        return ResponseEntity.badRequest().body(error);
    }
}
