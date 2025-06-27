package com.tuda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorApiResponse> handleApiException(ApiException ex) {
        ErrorApiResponse response = new ErrorApiResponse(
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorApiResponse> handleBadRequest(BadRequestException ex) {
        ErrorApiResponse response = new ErrorApiResponse(
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorApiResponse> handleNotFound(NotFoundException ex) {
        ErrorApiResponse response = new ErrorApiResponse(
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorApiResponse response = new ErrorApiResponse(
                errorMessage
        );
        return ResponseEntity.badRequest().body(response);
    }
}
