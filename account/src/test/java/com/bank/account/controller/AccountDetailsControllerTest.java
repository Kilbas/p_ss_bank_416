package com.bank.account.controller;

import com.bank.account.DTO.AccountDetailsDTO;
import com.bank.account.mapper.AccountDetailsMapper;
import com.bank.account.mapper.AccountDetailsMapperImpl;
import com.bank.account.service.AccountDetailsService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountDetailsController.class)
class AccountDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Spy
    private AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO();

    @MockBean
    private AccountDetailsService accountDetailsService;

    @InjectMocks
    private AccountDetailsController accountController;

    public AccountDetailsControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    @DisplayName("Тест на успешное сохранение информации об аккаунте")
    void testSaveAccountDetails_Success() throws Exception {
        // Arrange
        //AccountDetailsDTO inputDTO = new AccountDetailsDTO(1L, 1L, 1L, 1L, new BigDecimal("1000.00"), false, 1L);
        AccountDetailsDTO savedDTO = new AccountDetailsDTO(1L, 1L, 1L, 1L, new BigDecimal("1000.00"), false, 1L);

        when(accountDetailsService.saveAccountDetails(any(AccountDetailsDTO.class))).thenReturn(savedDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": 1,
                                    "passportId": 1,
                                    "accountNumber": 1,
                                    "bankDetailsId": 1,
                                    "money": 1000.00,
                                    "negativeBalance": false,
                                    "profileId": 1
                                }"""))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.money").value(1000.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.negativeBalance").value(false));
    }

    @Test
    @DisplayName("Тест на ошибку сервера при сохранении информации об аккаунте")
    void testSaveAccountDetails_ServerError() throws Exception {
        // Arrange
        when(accountDetailsService.saveAccountDetails(any(AccountDetailsDTO.class)))
                .thenThrow(new RuntimeException("Ошибка сервера"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": 1,
                                    "passportId": 1,
                                    "accountNumber": 1,
                                    "bankDetailsId": 1,
                                    "money": 1000.00,
                                    "negativeBalance": false,
                                    "profileId": 1
                                }"""))
                .andExpect(status().isInternalServerError());
    }
}