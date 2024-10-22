package com.voxloud.provisioning.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Map<String, List<String>>> handleGeneralExceptions(Exception ex) {
        List<String> errors = new ArrayList<>(Collections.singleton(ex.getMessage()));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorsMap(errors));
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Map<String, List<String>>> handleRuntimeExceptions(RuntimeException ex) {
        List<String> errors = new ArrayList<>(Collections.singleton(ex.getMessage()));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorsMap(errors));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleNotFoundException(NotFoundException ex) {
        List<String> errors = new ArrayList<>(Collections.singleton(ex.getMessage()));

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorsMap(errors));
    }

    @ExceptionHandler(GeneratingFileException.class)
    public ResponseEntity<Map<String, List<String>>> handleGeneratingFileException(GeneratingFileException ex) {
        List<String> errors = new ArrayList<>(Collections.singleton(ex.getMessage()));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorsMap(errors));
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<Map<String, List<String>>> handleUnsupportedOperationExceptionError(UnsupportedOperationException ex) {
        List<String> errors = new ArrayList<>(Collections.singleton(ex.getMessage()));

        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(errorsMap(errors));
    }

    private Map<String, List<String>> errorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
