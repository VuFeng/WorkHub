package com.workhub.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.workhub.server.dto.response.ApiResponse;
import com.workhub.server.dto.response.ErrorResponse;
import com.workhub.server.exception.custom.CompanyNotFoundException;
import com.workhub.server.exception.custom.DuplicateCompanyNameException;
import com.workhub.server.exception.custom.DuplicateEmailException;
import com.workhub.server.exception.custom.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // User Exceptions
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {
        ErrorResponse errorDetails = ErrorResponse.builder()
                .error("User Not Found")
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        ApiResponse<ErrorResponse> response = ApiResponse.error("User not found", errorDetails);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleDuplicateEmailException(
            DuplicateEmailException ex, WebRequest request) {
        ErrorResponse errorDetails = ErrorResponse.builder()
                .error("Duplicate Email")
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        ApiResponse<ErrorResponse> response = ApiResponse.error("Email already exists", errorDetails);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Company Exceptions
    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleCompanyNotFoundException(
            CompanyNotFoundException ex, WebRequest request) {
        ErrorResponse errorDetails = ErrorResponse.builder()
                .error("Company Not Found")
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        ApiResponse<ErrorResponse> response = ApiResponse.error("Company not found", errorDetails);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateCompanyNameException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleDuplicateCompanyNameException(
            DuplicateCompanyNameException ex, WebRequest request) {
        ErrorResponse errorDetails = ErrorResponse.builder()
                .error("Duplicate Company Name")
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        ApiResponse<ErrorResponse> response = ApiResponse.error("Company name already exists", errorDetails);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // General Exceptions

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        ErrorResponse errorDetails = ErrorResponse.builder()
                .error("Bad Request")
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        ApiResponse<ErrorResponse> response = ApiResponse.error("Invalid request", errorDetails);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleGeneralException(
            Exception ex, WebRequest request) {
        ErrorResponse errorDetails = ErrorResponse.builder()
                .error("Internal Server Error")
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        ApiResponse<ErrorResponse> response = ApiResponse.error("An unexpected error occurred", errorDetails);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
