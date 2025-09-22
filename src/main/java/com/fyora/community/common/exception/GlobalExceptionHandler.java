package com.fyora.community.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    record ErrorPayload(Instant timestamp, int status, String error, Object details) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorPayload> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fields = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fields.put(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(new ErrorPayload(Instant.now(), 400, "ValidationError", fields));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorPayload> notFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorPayload(Instant.now(), 404, "NotFound", ex.getMessage()));
    }

    @ExceptionHandler({BusinessException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ErrorPayload> business(RuntimeException ex) {
        return ResponseEntity.status(422).body(new ErrorPayload(Instant.now(), 422, "BusinessRule", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorPayload> generic(Exception ex) {
        return ResponseEntity.status(500).body(new ErrorPayload(Instant.now(), 500, "InternalError", "Unexpected error"));
    }
}
