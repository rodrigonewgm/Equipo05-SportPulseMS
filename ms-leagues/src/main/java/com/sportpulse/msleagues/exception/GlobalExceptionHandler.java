package com.sportpulse.msleagues.exception;

import com.sportpulse.msleagues.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LeagueNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLeagueNotFound(){

        ErrorResponse error = ErrorResponse.builder()
                .error("LEAGUE_NOT_FOUND")
                .message("No existe una liga con el ID proporcinado")
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
