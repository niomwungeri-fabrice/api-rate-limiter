package com.oltranz.apiratelimiter.exceptions;

import com.oltranz.apiratelimiter.dtos.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.builder()
                        .data(ex.getMessage())
                        .message(ex.getMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<?> handleRateLimitException(RateLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
                ApiResponse.builder()
                        .data(ex.getMessage())
                        .message(ex.getMessage())
                        .status(HttpStatus.TOO_MANY_REQUESTS.value())
                        .build()
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflictException(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponse.builder()
                        .data(ex.getMessage())
                        .message(ex.getMessage())
                        .status(HttpStatus.CONFLICT.value())
                        .build()
        );
    }
}
