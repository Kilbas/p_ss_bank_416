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

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<List<String>> handleEntityExceptions(DataAccessException ex) {

        List<String> error = new ArrayList<>();
        error.add(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<List<String>> handleEntityExceptions(EntityNotFoundException ex) {

        List<String> error = new ArrayList<>();
        error.add(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<List<String>> handleMappingJsonEntityExceptions(JsonProcessingException ex) {

        List<String> error = new ArrayList<>();
        error.add("При маппинге Json Entity в аудит произошла ошибка: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<List<String>> handleAllExceptions(Exception ex) {
//
//        List<String> error = new ArrayList<>();
//        error.add("Непредвиденная ошибка: " + ex.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }
}
