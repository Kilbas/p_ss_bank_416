package com.bank.account.controller;

import com.bank.account.DTO.AccountDetailsDTO;
import com.bank.account.service.AccountDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountDetailsController.class)
class AccountDetailsControllerTest {

    private AccountDetailsDTO accountDetailsDTO;
    private AccountDetailsDTO updateAccountDetailsDTO;

    public final String jsonFull = """
                                {
                                    "id": 1,
                                    "passportId": 1,
                                    "accountNumber": 1,
                                    "bankDetailsId": 1,
                                    "money": 18000000.00,
                                    "negativeBalance": false,
                                    "profileId": 1
                                }""";

    private final Long id = 1L;

    private static final BigDecimal money = BigDecimal.valueOf(18000000.00);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountDetailsService accountDetailsService;

    @InjectMocks
    private AccountDetailsController accountController;

    @BeforeEach
    void setUp() {
        accountDetailsDTO = createAccountDetailsDTO();
        updateAccountDetailsDTO = new AccountDetailsDTO();
    }

    private AccountDetailsDTO createAccountDetailsDTO() {
        return new AccountDetailsDTO(null, 1L, 1L, 1L, money, false, 1L);
    }

    public AccountDetailsControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Nested
    @DisplayName("Тесты для метода saveAccountDetails")
    class saveAccountDetailsTest {
        @Test
        @DisplayName("Тест на успешное сохранение информации об аккаунте")
        void testSaveAccountDetails_Success() throws Exception {

            accountDetailsDTO.setId(1L);

            when(accountDetailsService.saveAccountDetails(any(AccountDetailsDTO.class))).thenReturn(accountDetailsDTO);

            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonFull))
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.money").value(18000000.00))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.negativeBalance").value(false));
        }

        @Test
        @DisplayName("Тест на ошибку сервера при сохранении информации об аккаунте")
        void testSaveAccountDetails_ServerError() throws Exception {

            when(accountDetailsService.saveAccountDetails(any(AccountDetailsDTO.class)))
                    .thenThrow(new RuntimeException("Ошибка сервера"));

            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonFull))
                    .andExpect(status().isInternalServerError());
        }
    }
}