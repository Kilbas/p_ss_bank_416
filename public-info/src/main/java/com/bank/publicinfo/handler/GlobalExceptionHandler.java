package com.bank.publicinfo.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GlobalExceptionHandler обрабатывает исключения, возникающие в контроллерах приложения.
 * Этот класс использует аннотацию {@link RestControllerAdvice} для глобальной обработки
 * исключений и возврата соответствующих ответов клиенту.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения валидации, возникающие при указании неверных аргументов сущности.
     *
     * @param ex исключение, содержащее информацию о нарушениях валидации
     * @return ResponseEntity с кодом статуса 400 (BAD REQUEST) и картой ошибок
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Обрабатывает исключения доступа к данным.
     *
     * @param ex исключение, связанное с доступом к данным
     * @return ResponseEntity с кодом статуса 422 (UNPROCESSABLE ENTITY) и списком ошибок
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<List<String>> handleEntityExceptions(DataAccessException ex) {

        List<String> error = new ArrayList<>();
        error.add(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    /**
     * Обрабатывает исключения, когда сущность не найдена.
     *
     * @param ex исключение, возникающее при отсутствии сущности
     * @return ResponseEntity с кодом статуса 404 (NOT FOUND) и списком ошибок
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<List<String>> handleEntityExceptions(EntityNotFoundException ex) {

        List<String> error = new ArrayList<>();
        error.add(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Обрабатывает исключения, возникающие при обработке JSON.
     *
     * @param ex исключение, связанное с ошибками маппинга JSON
     * @return ResponseEntity с кодом статуса 500 (INTERNAL SERVER ERROR) и сообщением об ошибке
     */
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<List<String>> handleMappingJsonEntityExceptions(JsonProcessingException ex) {

        List<String> error = new ArrayList<>();
        error.add("При маппинге Json Entity в аудит произошла ошибка: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Обрабатывает все остальные непредвиденные исключения.
     *
     * @param ex непредвиденное исключение
     * @return ResponseEntity с кодом статуса 500 (INTERNAL SERVER ERROR) и сообщением об ошибке
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<List<String>> handleAllExceptions(Exception ex) {

        List<String> error = new ArrayList<>();
        error.add("Непредвиденная ошибка: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}