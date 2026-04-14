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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex){

        if("USER_ALREADY_EXISTS".equals(ex.getMessage())){
            Map<String, Object> error = new HashMap<>();
            error.put("error", "USER_ALREADY_EXISTS");
            error.put("message", "Ya existe un usuario con ese email");
            error.put("timestamp", Instant.now());

            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        if("USERNAME_ALREADY_EXISTS".equals(ex.getMessage())){
            Map<String, Object> error = new HashMap<>();
            error.put("error", "USERNAME_ALREADY_EXISTS");
            error.put("message", "Ya existe un usuario con ese username");
            error.put("timestamp", Instant.now());

            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
