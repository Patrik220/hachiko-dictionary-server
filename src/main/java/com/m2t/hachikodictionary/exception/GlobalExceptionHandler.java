package com.m2t.hachikodictionary.exception;

import com.m2t.hachikodictionary.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidCredentialsException.class, AccountNotFoundException.class, InvalidTokenException.class})
    public <T extends RuntimeException> ResponseEntity<Object> handleUnauthorizedExceptions(T exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new Response(false, exception.getMessage()));
    }

    @ExceptionHandler({PasswordsDoNotMatchException.class, UsernameAlreadyExistsException.class, EmailAlreadyExistsException.class})
    public <T extends RuntimeException> ResponseEntity<Object> handleBadRequestExceptions(T exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new Response(false, exception.getMessage()));
    }

    @ExceptionHandler({ConfirmationNotFoundException.class})
    public <T extends RuntimeException> ResponseEntity<Object> handleNotFoundExceptions(T exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new Response(false, exception.getMessage()));
    }
}