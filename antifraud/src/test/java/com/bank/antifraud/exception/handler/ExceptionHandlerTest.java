package com.bank.antifraud.exception.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionHandlerTest {

    @InjectMocks
    private ExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should handle EntityNotFoundException")
    void testHandleEntityNotFoundException() {
        // Arrange
        String errorMessage = "Запись не найдена";
        EntityNotFoundException exception = new EntityNotFoundException(errorMessage);

        // Act
        ResponseEntity<Object> response = exceptionHandler.handleEntityNotFoundException(exception);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(404);

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        assertThat(responseBody.get("статус")).isEqualTo(404);
        assertThat(responseBody.get("ошибка")).isEqualTo("Not Found");
        assertThat(responseBody.get("сообщение")).isEqualTo(errorMessage);
        assertThat(responseBody).containsKey("временная метка");
    }


    @Test
    @DisplayName("Should handle MethodArgumentTypeMismatchException")
    void testHandleTypeMismatchException() {
        // Arrange
        String parameterName = "id";
        String parameterValue = "abc";
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                parameterValue, Long.class, parameterName, null, null
        );
        // Act
        ResponseEntity<Object> response = exceptionHandler.handleTypeMismatchException(exception);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(400);

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        assertThat(responseBody).containsEntry("статус", 400);
        assertThat(responseBody).containsEntry("ошибка", "Bad Request");
        assertThat(responseBody).containsEntry(
                "сообщение", "Некорректный тип параметра 'id'. Ожидался тип 'Long', но получено 'abc'."
        );
    }

    @Test
    @DisplayName("Should handle DataIntegrityViolationException")
    void testHandleDataIntegrityViolationException() {
        // Arrange
        String specificCauseMessage = "Нарушение уникального ограничения";
        DataIntegrityViolationException exception = new DataIntegrityViolationException(
                "Ошибка целостности данных", new Throwable(specificCauseMessage)
        );

        // Act
        ResponseEntity<Object> response = exceptionHandler.handleDataIntegrityViolation(exception);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(422);

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        assertThat(responseBody).containsEntry("статус", 422);
        assertThat(responseBody).containsEntry("ошибка", "Unprocessable Entity");
        assertThat(responseBody).containsEntry(
                "сообщение", "Ошибка целостности данных: " + specificCauseMessage
        );
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException")
    void testHandleIllegalArgumentException() {
        // Arrange
        String errorMessage = "Неверный аргумент";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        // Act
        ResponseEntity<Object> response = exceptionHandler.handleIllegalArgumentException(exception);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(400);

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        assertThat(responseBody).containsEntry("статус", 400);
        assertThat(responseBody).containsEntry("ошибка", "Bad Request");
        assertThat(responseBody).containsEntry("сообщение", errorMessage);
    }

    @Test
    @DisplayName("Should handle generic Exception")
    void testHandleGenericException() {
        // Arrange
        Exception exception = new Exception("Unexpected error");

        // Act
        ResponseEntity<Object> response = exceptionHandler.handleAllExceptions(exception);

        // Assert
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        assertThat(responseBody).containsEntry("статус", 500);
        assertThat(responseBody).containsEntry("ошибка", "Internal Server Error");
        assertThat(responseBody).containsEntry(
                "сообщение", "Внутренняя ошибка сервера. Пожалуйста, свяжитесь с поддержкой."
        );
    }
}

