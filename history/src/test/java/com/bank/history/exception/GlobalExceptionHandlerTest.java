package com.bank.history.exception;

import com.bank.history.controllers.HistoryController;
import com.bank.history.dto.HistoryDTO;
import com.bank.history.services.HistoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.EntityNotFoundException;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoryService historyService;

    private static final String invalidJson = "{ \"invalid\": \"data\" }";

    @Test
    @DisplayName("Тест обработки EntityNotFoundException")
    void testHandleEntityNotFoundException() throws Exception {

        doThrow(new EntityNotFoundException("Сущность не найдена"))
                .when(historyService).findById(anyLong());

        mockMvc.perform(get("/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Сущность не найдена"));
    }

    @Test
    @DisplayName("Тест обработки HttpMessageNotReadableException")
    void testHandleHttpMessageNotReadableException() throws Exception {

        doThrow(new HttpMessageNotReadableException("Invalid data"))
                .when(historyService).save(any());

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid data"));
    }

    @Test
    @DisplayName("Тест обработки MethodArgumentNotValidException")
    void testHandleMethodArgumentNotValidException() throws Exception {

        Method method = HistoryController.class.getMethod("saveHistory",
                HistoryDTO.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        BindingResult bindingResult = new BeanPropertyBindingResult(new HistoryDTO(), "historyDTO");
        bindingResult.rejectValue("transferAuditId", "NotNull", "Поле 'transferAuditId' не может быть пустым");
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        ResponseEntity<String> response = globalExceptionHandler.handleMethodArgumentNotValidException(exception);
        assertAll(
                () -> assertNotNull(response, "Ответ не должен быть null"),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Должен быть статус 400 BAD_REQUEST"),
                () -> assertTrue(response.getBody().contains("Поле 'transferAuditId' не может быть пустым"),
                        "Сообщение должно содержать информацию об ошибке")
        );
    }
}
