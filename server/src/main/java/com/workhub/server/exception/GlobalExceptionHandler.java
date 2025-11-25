package com.workhub.server.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.workhub.server.dto.response.ApiResponse;
import com.workhub.server.dto.response.ErrorResponse;
import com.workhub.server.exception.custom.CompanyNotFoundException;
import com.workhub.server.exception.custom.DuplicateCompanyNameException;
import com.workhub.server.exception.custom.DuplicateEmailException;
import com.workhub.server.exception.custom.JobNotFoundException;
import com.workhub.server.exception.custom.TaskCommentNotFoundException;
import com.workhub.server.exception.custom.TaskNotFoundException;
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

        // Job Exceptions
        @ExceptionHandler(JobNotFoundException.class)
        public ResponseEntity<ApiResponse<ErrorResponse>> handleJobNotFoundException(
                        JobNotFoundException ex, WebRequest request) {
                ErrorResponse errorDetails = ErrorResponse.builder()
                                .error("Job Not Found")
                                .message(ex.getMessage())
                                .status(HttpStatus.NOT_FOUND.value())
                                .path(request.getDescription(false).replace("uri=", ""))
                                .build();

                ApiResponse<ErrorResponse> response = ApiResponse.error("Job not found", errorDetails);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Task Exceptions
        @ExceptionHandler(TaskNotFoundException.class)
        public ResponseEntity<ApiResponse<ErrorResponse>> handleTaskNotFoundException(
                        TaskNotFoundException ex, WebRequest request) {
                ErrorResponse errorDetails = ErrorResponse.builder()
                                .error("Task Not Found")
                                .message(ex.getMessage())
                                .status(HttpStatus.NOT_FOUND.value())
                                .path(request.getDescription(false).replace("uri=", ""))
                                .build();

                ApiResponse<ErrorResponse> response = ApiResponse.error("Task not found", errorDetails);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // TaskComment Exceptions
        @ExceptionHandler(TaskCommentNotFoundException.class)
        public ResponseEntity<ApiResponse<ErrorResponse>> handleTaskCommentNotFoundException(
                        TaskCommentNotFoundException ex, WebRequest request) {
                ErrorResponse errorDetails = ErrorResponse.builder()
                                .error("Task Comment Not Found")
                                .message(ex.getMessage())
                                .status(HttpStatus.NOT_FOUND.value())
                                .path(request.getDescription(false).replace("uri=", ""))
                                .build();

                ApiResponse<ErrorResponse> response = ApiResponse.error("Comment not found", errorDetails);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // General Exceptions

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationException(
                        MethodArgumentNotValidException ex, WebRequest request) {
                List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> ErrorResponse.ValidationError.builder()
                                                .field(error.getField())
                                                .message(error.getDefaultMessage())
                                                .rejectedValue(error.getRejectedValue())
                                                .build())
                                .collect(Collectors.toList());

                ErrorResponse errorDetails = ErrorResponse.builder()
                                .error("Validation Failed")
                                .message("Invalid request parameters")
                                .status(HttpStatus.BAD_REQUEST.value())
                                .path(request.getDescription(false).replace("uri=", ""))
                                .validationErrors(validationErrors)
                                .build();

                ApiResponse<ErrorResponse> response = ApiResponse.error("Validation failed", errorDetails);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ApiResponse<ErrorResponse>> handleBadCredentialsException(
                        BadCredentialsException ex, WebRequest request) {
                ErrorResponse errorDetails = ErrorResponse.builder()
                                .error("Authentication Failed")
                                .message(ex.getMessage())
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .path(request.getDescription(false).replace("uri=", ""))
                                .build();

                ApiResponse<ErrorResponse> response = ApiResponse.error("Authentication failed", errorDetails);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiResponse<ErrorResponse>> handleAccessDeniedException(
                        AccessDeniedException ex, WebRequest request) {
                ErrorResponse errorDetails = ErrorResponse.builder()
                                .error("Forbidden")
                                .message(ex.getMessage())
                                .status(HttpStatus.FORBIDDEN.value())
                                .path(request.getDescription(false).replace("uri=", ""))
                                .build();

                ApiResponse<ErrorResponse> response = ApiResponse.error("Access denied", errorDetails);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

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
