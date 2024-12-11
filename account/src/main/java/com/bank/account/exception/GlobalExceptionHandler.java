package com.bank.account.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        log.warn(message);
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String errorMessage = "Ошибка разбора JSON: " + ex.getMessage();
        return buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String errorMessage = "Ошибка целостности данных: " + ex.getMostSpecificCause().getMessage();
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        String errorMessage = "Ошибка валидации: " + String.join(", ", errors);
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, errorMessage);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        String errorMessage = "Произошла непредвиденная ошибка: " + ex.getMessage();
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
    }
}
