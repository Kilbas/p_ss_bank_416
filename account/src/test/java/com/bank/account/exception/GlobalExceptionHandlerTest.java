package com.bank.account.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Тест обработки MethodArgumentNotValidException: ошибка валидации")
    void testHandleMethodArgumentNotValidException() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "Поле не может быть пустым");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        MethodParameter methodParameter = mock(MethodParameter.class);
        when(methodParameter.getExecutable()).thenReturn(GlobalExceptionHandler.class.getDeclaredMethod("handleMethodArgumentNotValidException", MethodArgumentNotValidException.class));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentNotValidException(ex);

        assertAll(
                () -> assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Ошибка валидации: Поле не может быть пустым", response.getBody().getMessage()),
                () -> assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getBody().getStatus())
        );
    }

    @Test
    @DisplayName("Тест обработки IllegalArgumentException: ошибка целостности данных")
    void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Объект accountDetailsDTO не может быть null");

        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgumentException(ex);

        assertAll(
                () -> assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Ошибка целостности данных: Объект accountDetailsDTO не может быть null", response.getBody().getMessage()),
                () -> assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getBody().getStatus())
        );
    }
}
