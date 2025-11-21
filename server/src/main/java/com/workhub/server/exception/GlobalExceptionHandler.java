package com.workhub.server.exception;

import java.util.Map;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.workhub.server.exception.custom.DuplicateEmailException;
import com.workhub.server.exception.custom.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleUserNotFoundException(UserNotFoundException ex) {
        return Map.of(
                "error", "User not found",
                "message", ex.getMessage(),
                "status", HttpStatus.NOT_FOUND.value(),
                "timestamp", LocalDateTime.now().toString());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleDuplicateEmailException(DuplicateEmailException ex) {
        return Map.of(
                "error", "Duplicate email",
                "message", ex.getMessage(),
                "status", HttpStatus.CONFLICT.value(),
                "timestamp", LocalDateTime.now().toString());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGeneralException(Exception ex) {
        return Map.of(
                "error", "Internal server error",
                "message", ex.getMessage(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "timestamp", LocalDateTime.now().toString());
    }
}
