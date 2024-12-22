package com.bank.publicinfo.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс для тестирование GlobalExceptionHandler.")
class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        this.globalExceptionHandler = new GlobalExceptionHandler();
    }

    //Этот обработчик не смог придумать как протестить в контроллере. но все остальные там тестирую.
    @Test
    @DisplayName("Обработка исключения JsonProcessingException при маппинге обьекта в json.")
    void handleMappingJsonEntityExceptions_ShouldReturnInternalServerError() {
        JsonProcessingException ex = new JsonProcessingException("JSON processing error") {
        };

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMappingJsonEntityExceptions(ex);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("JsonProcessingException", Objects.requireNonNull(response.getBody()).getCause());
        assertEquals("При маппинге Json Entity в аудит произошла ошибка: JSON processing error", response.getBody().getMessage());
    }
}