package com.hirepath.hirepath_backend.security;

import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final Environment environment;

    public GlobalExceptionHandler(Environment environment) {
        this.environment = environment;
        logger.info("GlobalExceptionHandler initialized");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseFormat> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation failed");
        logger.warn("Validation error for user: {} at {} - {}",
                getCurrentUser(), getRequestUri(request), message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseFormat.createFailResponse(null, message));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseFormat> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        logger.warn("Client error [{}] for user: {} at {} - {}",
                ex.getStatusCode(), getCurrentUser(), getRequestUri(request), ex.getReason());
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ResponseFormat.createFailResponse(null, ex.getReason()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseFormat> handleDataAccessException(DataAccessException ex, WebRequest request) {
        logger.error("Database error for user: {} at {} - {}",
                getCurrentUser(), getRequestUri(request), ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseFormat.createFailResponse(null, "Database error occurred"));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ResponseFormat> handleJwtException(JwtException ex, WebRequest request) {
        logger.error("JWT validation error for user: {} at {} - {}",
                getCurrentUser(), getRequestUri(request), ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ResponseFormat.createFailResponse(null, "Invalid or expired token"));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseFormat> handleGenericException(Throwable ex, WebRequest request) {
        String message = isDevEnvironment() ? ex.getMessage() : "An unexpected error occurred";
        logger.error("Unexpected error for user: {} at {} - {}",
                getCurrentUser(), getRequestUri(request), ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseFormat.createFailResponse(null, message));
    }

    private String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "anonymous";
    }

    private boolean isDevEnvironment() {
        return Arrays.asList(environment.getActiveProfiles()).contains("dev");
    }

    private String getRequestUri(WebRequest request) {
        return request instanceof ServletWebRequest
                ? ((ServletWebRequest) request).getRequest().getRequestURI()
                : "unknown";
    }
}
