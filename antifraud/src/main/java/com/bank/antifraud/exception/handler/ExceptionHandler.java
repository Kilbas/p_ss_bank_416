package com.bank.antifraud.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ExceptionHandler {

    // Универсальный метод для формирования ответа
    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("статус", status.value());
        response.put("ошибка", status.getReasonPhrase());
        response.put("сообщение", message);
        response.put("временная метка", System.currentTimeMillis());
        return ResponseEntity.status(status).body(response);
    }

    // Обработчик для некорректного типа аргументов
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String requiredType = (ex.getRequiredType() != null) ? ex.getRequiredType().getSimpleName() : "неизвестный тип";
        String errorMessage = String.format("Некорректный тип параметра '%s'. Ожидался тип '%s', но получено '%s'.",
                ex.getName(), requiredType, ex.getValue());
        return buildResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }


    // Обработчик для MethodArgumentNotValidException и HttpMessageNotReadableException
    @org.springframework.web.bind.annotation.ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleBadRequestExceptions(Exception ex) {
        String errorMessage;

        if (ex instanceof MethodArgumentNotValidException) {
            errorMessage = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        } else {
            errorMessage = "Ошибка разбора JSON: " + ex.getMessage();
        }

        return buildResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    // Обработчик для DataIntegrityViolationException
    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Ошибка целостности данных: " + ex.getMostSpecificCause().getMessage();
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    // Обработчик для EntityNotFoundException
    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Общий обработчик для всех исключений
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        String errorMessage = "Внутренняя ошибка сервера. Пожалуйста, свяжитесь с поддержкой.";
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Illegal argument exception: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }


}