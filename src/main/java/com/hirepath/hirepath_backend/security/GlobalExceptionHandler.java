package com.hirepath.hirepath_backend.security;

import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Environment environment;

    public GlobalExceptionHandler(Environment environment) {
        this.environment = environment;
        log.info("GlobalExceptionHandler initialized");
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ResponseFormat> handleAuthorizationDeniedException(AuthorizationDeniedException ex, WebRequest request) {
        log.warn("Access denied for user: {} at {} - {}",
                getCurrentUser(), getRequestUri(request), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ResponseFormat.createFailResponse(null, "Unauthorized user"));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseFormat> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
        log.warn("No handler found for user: {} at {} - {}",
                getCurrentUser(), getRequestUri(request), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseFormat.createFailResponse(null, "API not found"));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseFormat> handleNoResourceFoundException(NoResourceFoundException ex, WebRequest request) {
        log.warn("No resource found for user: {} at {} - {}",
                getCurrentUser(), getRequestUri(request), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseFormat.createFailResponse(null, "API not found"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseFormat> httpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        log.warn("No handler found for user: {} at {} - {}",
                getCurrentUser(), getRequestUri(request), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseFormat.createFailResponse(null, "Bad request"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseFormat> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation failed");
        log.warn("Validation error for user: {} at {} - {}",
                getCurrentUser(), getRequestUri(request), message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseFormat.createFailResponse(null, message));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseFormat> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        log.warn("Client error [{}] for user: {} at {} - {}",
                ex.getStatusCode(), getCurrentUser(), getRequestUri(request), ex.getReason());
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ResponseFormat.createFailResponse(null, ex.getReason()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseFormat> handleDataAccessException(DataAccessException ex, WebRequest request) {
        log.error("Database error for user: {} at {} - {}",
                getCurrentUser(), getRequestUri(request), ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseFormat.createFailResponse(null, "Database error occurred"));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ResponseFormat> handleJwtException(JwtException ex, WebRequest request) {
        log.error("JWT validation error for user: {} at {} - {}",
                getCurrentUser(), getRequestUri(request), ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ResponseFormat.createFailResponse(null, "Invalid or expired token"));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseFormat> handleGenericException(Throwable ex, WebRequest request) {
        String message = isDevEnvironment() ? ex.getMessage() : "An unexpected error occurred";
        log.error("Unexpected error for user: {} at {} - {}",
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
