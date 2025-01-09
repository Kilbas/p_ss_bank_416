package com.bank.transfer.controller.validationHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidationHandlerTest {
    @InjectMocks
    private ValidationExceptionHandler validationExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleValidationExceptionsTest_shouldReturnErrorMap_whenValidationFails() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("accountTransfer", "number", "Номер счёта должен быть положительным");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, String>> response = validationExceptionHandler.handleValidationExceptions(exception);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("number", "Номер счёта должен быть положительным");
        assertThat(response.getBody()).isEqualTo(expectedErrors);
    }

    @Test
    void handleValidationExceptionsTest_shouldHandleMultipleErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("accountTransfer", "number", "Номер счёта должен быть положительным");
        FieldError fieldError2 = new FieldError("accountTransfer", "amount", "Сумма перевода должна быть больше 0.01");
        FieldError fieldError3 = new FieldError("accountTransfer", "accountDetailsId", "ID должен быть положительным");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2, fieldError3));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, String>> response = validationExceptionHandler.handleValidationExceptions(exception);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("number", "Номер счёта должен быть положительным");
        expectedErrors.put("amount", "Сумма перевода должна быть больше 0.01");
        expectedErrors.put("accountDetailsId", "ID должен быть положительным");
        assertThat(response.getBody()).isEqualTo(expectedErrors);
    }
}
