package com.bank.antifraud.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ValidationExceptionHandler {
    // Универсальный метод для формирования ответа
    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(status).body(response);
    }

    // Обработчик для MethodArgumentNotValidException и HttpMessageNotReadableException
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleBadRequestExceptions(Exception ex) {
        String errorMessage;

        if (ex instanceof MethodArgumentNotValidException) {
            errorMessage = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList())
                    .toString();
        } else {
            errorMessage = "Ошибка разбора JSON: " + ex.getMessage();
        }

        return buildResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    // Обработчик для DataIntegrityViolationException
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Ошибка целостности данных: " + ex.getMostSpecificCause().getMessage();
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    // Обработчик для EntityNotFoundException
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Общий обработчик для всех исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        String errorMessage = "Внутренняя ошибка сервера. Пожалуйста, свяжитесь с поддержкой.";
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
    }

}
