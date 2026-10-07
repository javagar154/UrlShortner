package com.example.urlshortener.controller;

import com.example.urlshortener.model.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Centralised error handling — catches validation failures and malformed JSON
 * so the application never crashes and always returns a clean JSON error body.
 *
 * @RestControllerAdvice applies this handler to all @RestController classes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Fired when @Valid fails on a request body field
     * (e.g. blank URL, missing "url" field, URL that doesn't start with http).
     *
     * Collects all field-level error messages into one comma-separated string
     * so the caller knows exactly what went wrong.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, message));
    }

    /**
     * Fired when the request body is missing entirely or contains invalid JSON
     * (e.g. sending an empty body or raw text instead of JSON).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, "Request body is missing or malformed JSON"));
    }
}
