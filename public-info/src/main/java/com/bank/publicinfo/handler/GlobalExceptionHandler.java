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
import java.util.List;

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
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<List<ErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorResponse> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> errors.add(new ErrorResponse(error.getField(), error.getDefaultMessage())));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Обрабатывает исключения доступа к данным.
     *
     * @param ex исключение, связанное с доступом к данным
     * @return ResponseEntity с кодом статуса 422 (UNPROCESSABLE ENTITY) и списком ошибок
     */
    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<ErrorResponse> handleEntityExceptions(DataAccessException ex) {
        ErrorResponse error = new ErrorResponse("DataAccessException", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    /**
     * Обрабатывает исключения, когда сущность не найдена.
     *
     * @param ex исключение, возникающее при отсутствии сущности
     * @return ResponseEntity с кодом статуса 404 (NOT FOUND) и списком ошибок
     */
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleEntityExceptions(EntityNotFoundException ex) {
        ErrorResponse response = new ErrorResponse("EntityNotFound", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Обрабатывает исключения, возникающие при обработке JSON.
     *
     * @param ex исключение, связанное с ошибками маппинга JSON
     * @return ResponseEntity с кодом статуса 500 (INTERNAL SERVER ERROR) и сообщением об ошибке
     */
    @ExceptionHandler({JsonProcessingException.class})
    public ResponseEntity<ErrorResponse> handleMappingJsonEntityExceptions(JsonProcessingException ex) {
        ErrorResponse error = new ErrorResponse("JsonProcessingException", "При маппинге Json Entity в аудит произошла ошибка: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Обрабатывает все остальные непредвиденные исключения.
     *
     * @param ex непредвиденное исключение
     * @return ResponseEntity с кодом статуса 500 (INTERNAL SERVER ERROR) и сообщением об ошибке
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        ErrorResponse error = new ErrorResponse("Exception", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}