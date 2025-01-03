package com.bank.account.controller;

import com.bank.account.DTO.AccountDetailsDTO;
import com.bank.account.service.AccountDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountDetailsController.class)
@ExtendWith(MockitoExtension.class)
class AccountDetailsControllerTest {

    private AccountDetailsDTO accountDetailsDTO;
    private AccountDetailsDTO updateAccountDetailsDTO;
    private ObjectMapper objectMapper;

    private static final String jsonFull = """
                                {
                                    "id": 1,
                                    "passportId": 1,
                                    "accountNumber": 1,
                                    "bankDetailsId": 1,
                                    "money": 18000000.00,
                                    "negativeBalance": false,
                                    "profileId": 1
                                }""";

    private static final String jsonUpdate = """
                                {
                                    "money": 28000000.00
                                }""";

    private static final String jsonNotCorrect = """
                                {
                                    "money": 28000000.00,
                                }""";

    private static final Long id = 1L;
    private static final int page = 0;
    private static final int size = 2;
    private static final BigDecimal money = BigDecimal.valueOf(18000000.00);
    private static final BigDecimal updateMoney = BigDecimal.valueOf(28000000.00);
    private static final String errorNotFound = "Тест на ошибку 404 информация не найдена";
    private static final String errorServerError = "Ошибка сервера";
    private static final String EntityNotFound = "Запись не найдена";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountDetailsService accountDetailsService;

    @BeforeEach
    void setUp() {
        accountDetailsDTO = createAccountDetailsDTO();
        updateAccountDetailsDTO = new AccountDetailsDTO();
        objectMapper = new ObjectMapper();
    }

    private AccountDetailsDTO createAccountDetailsDTO() {
        return new AccountDetailsDTO(null, id, id, id, money, false, id);
    }

    private void assertJsonResponseEquals(String jsonResponse, AccountDetailsDTO expected) throws Exception {
        AccountDetailsDTO actual = objectMapper.readValue(jsonResponse, AccountDetailsDTO.class);
        AccountDetailsControllerTest.assertEqualsAccountDetailsDTOJsonContent(actual, expected);
    }

    private void assertJsonResponseContentIndexEquals(String jsonResponse, int index, AccountDetailsDTO expected) throws Exception {
        String elementJson = objectMapper.writeValueAsString(JsonPath
                        .read(jsonResponse, "$.content[" + index + "]"));

        AccountDetailsControllerTest.assertEqualsAccountDetailsDTOJsonContent(objectMapper
                        .readValue(elementJson, AccountDetailsDTO.class), expected);
    }

    public static void assertEqualsAccountDetailsDTOJsonContent(AccountDetailsDTO result, AccountDetailsDTO accountDetailsDTO) {
        assertEquals(result, accountDetailsDTO);
    }

    @Nested
    @DisplayName("Тесты для метода saveAccountDetails")
    class saveAccountDetailsTest {

        @Test
        @DisplayName("Тест на успешное сохранение информации об аккаунте")
        void testSaveAccountDetailsPositive() throws Exception {
            accountDetailsDTO.setId(id);

            when(accountDetailsService.saveAccountDetails(any(AccountDetailsDTO.class))).thenReturn(accountDetailsDTO);

            MvcResult result = mockMvc.perform(post("/api/v1/account/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonFull))
                    .andExpect(status().isCreated())
                    .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();

            assertJsonResponseEquals(jsonResponse, accountDetailsDTO);
        }

        @Test
        @DisplayName("Тест на ошибку сервера: ошибка целостности данных")
        void testSaveAccountDetailsNegativeUnprocessableEntity() throws Exception {
            when(accountDetailsService.saveAccountDetails(any(AccountDetailsDTO.class)))
                    .thenThrow(new DataIntegrityViolationException(""));

            mockMvc.perform(post("/api/v1/account/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonFull))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.message").value("Ошибка целостности данных: "));
        }

        @Test
        @DisplayName("Тест на ошибку сервера при сохранении информации об аккаунте")
        void testSaveAccountDetailsNegativeServerError() throws Exception {
            when(accountDetailsService.saveAccountDetails(any(AccountDetailsDTO.class)))
                    .thenThrow(new RuntimeException(errorServerError));

            mockMvc.perform(post("/api/v1/account/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonFull))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.message").value("Произошла непредвиденная ошибка: Ошибка сервера"));
        }
    }

    @Nested
    @DisplayName("Тесты для метода updateAccountDetails")
    class updateAccountDetailsTest {

        @Test
        @DisplayName("Тест на успешное обновление информации об аккаунте")
        void testUpdateAccountDetailsPositive() throws Exception {
            accountDetailsDTO.setId(id);
            accountDetailsDTO.setMoney(updateMoney);

            when(accountDetailsService.updateAccountDetails(anyLong(), any(AccountDetailsDTO.class))).thenReturn(accountDetailsDTO);

            MvcResult result = mockMvc.perform(patch("/api/v1/account/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonUpdate))
                    .andExpect(status().isCreated())
                    .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();

            assertJsonResponseEquals(jsonResponse, accountDetailsDTO);
        }

        @Test
        @DisplayName("Тест на ошибку сервера: ошибка целостности данных")
        void testSaveAccountDetailsNegativeUnprocessableEntity() throws Exception {
            when(accountDetailsService.updateAccountDetails(anyLong(), any(AccountDetailsDTO.class)))
                    .thenThrow(new DataIntegrityViolationException(""));

            mockMvc.perform(patch("/api/v1/account/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonFull))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.message").value("Ошибка целостности данных: "));
        }

        @Test
        @DisplayName("Тест на ошибку сервера при обновлении информации об аккаунте")
        void testUpdateAccountDetailsNegativeServerError() throws Exception {
            updateAccountDetailsDTO.setId(id);

            when(accountDetailsService.updateAccountDetails(id, updateAccountDetailsDTO))
                    .thenThrow(new RuntimeException(errorServerError));

            mockMvc.perform(patch("/api/v1/account/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonUpdate))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.message").value("Произошла непредвиденная ошибка:" +
                            " Cannot invoke \"com.bank.account.DTO.AccountDetailsDTO.getId()\" because \"savedAccountDetailsDTO\" is null"));
        }

        @Test
        @DisplayName("Тест на ошибку 400 Bad Request при некорректном JSON")
        void testUpdateAccountDetailsNegativeBadRequest() throws Exception {
            mockMvc.perform(patch("/api/v1/account/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonNotCorrect))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Тесты для метода deleteAccountDetails")
    class deleteAccountDetailsTest {

        @Test
        @DisplayName("Тест на успешное удаление информации об аккаунте")
        void testDeleteAccountDetailsByIDPositive() throws Exception {
            doNothing().when(accountDetailsService).deleteAccountDetails(id);

            mockMvc.perform(delete("/api/v1/account/delete/{id}", id))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Тест на ошибку 404 информация не найдена")
        void testDeleteAccountDetailsByIDNegativeNotFound() throws Exception {
            doThrow(new EntityNotFoundException(EntityNotFound)).when(accountDetailsService).deleteAccountDetails(id);

            mockMvc.perform(delete("/api/v1/account/delete/{id}", id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(EntityNotFound));
        }

        @Test
        @DisplayName("Тест на ошибку сервера при удалении информации об аккаунте")
        void testDeleteAccountDetailsByIDNegativeServerError() throws Exception {
            doThrow(new RuntimeException(errorServerError)).when(accountDetailsService).deleteAccountDetails(id);

            mockMvc.perform(delete("/api/v1/account/delete/{id}", id))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("Тесты для метода getAccountDetailsByID")
    class getAccountDetailsByIDTest {

        @Test
        @DisplayName("Тест на успешный поиск информации об аккаунте по идентификатору")
        void testGetAccountDetailsByIDPositive() throws Exception {
            accountDetailsDTO.setId(id);

            when(accountDetailsService.getAccountDetailsById(id)).thenReturn(accountDetailsDTO);

            MvcResult result = mockMvc.perform(get("/api/v1/account/id/{id}", id))
                    .andExpect(status().isOk())
                    .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();

            assertJsonResponseEquals(jsonResponse, accountDetailsDTO);
        }

        @Test
        @DisplayName(errorNotFound)
        void testGetAccountDetailsByIDNegativeNotFound() throws Exception {
            doThrow(new EntityNotFoundException(EntityNotFound)).when(accountDetailsService).getAccountDetailsById(id);

            mockMvc.perform(get("/api/v1/account/id/{id}", id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(EntityNotFound));
        }
    }

    @Nested
    @DisplayName("Тесты для метода getAccountDetailsByAccountNumber")
    class getAccountDetailsByAccountNumberTest {

        @Test
        @DisplayName("Тест на успешный поиск информации об аккаунте по номеру счёта")
        void testGetAccountDetailsByAccountNumberPositive() throws Exception {
            accountDetailsDTO.setId(id);

            when(accountDetailsService.getAccountDetailsByAccountNumber(accountDetailsDTO.getAccountNumber())).thenReturn(accountDetailsDTO);

            MvcResult result = mockMvc.perform(get("/api/v1/account/accountNumber/{accountNumber}", accountDetailsDTO.getAccountNumber()))
                    .andExpect(status().isOk())
                    .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();

            assertJsonResponseEquals(jsonResponse, accountDetailsDTO);
        }

        @Test
        @DisplayName(errorNotFound)
        void testGetAccountDetailsByAccountNumberNegativeNotFound() throws Exception {
            doThrow(new EntityNotFoundException(EntityNotFound)).when(accountDetailsService).getAccountDetailsByAccountNumber(accountDetailsDTO.getAccountNumber());

            mockMvc.perform(get("/api/v1/account/accountNumber/{accountNumber}", accountDetailsDTO.getAccountNumber()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(EntityNotFound));
        }
    }

    @Nested
    @DisplayName("Тесты для метода getAccountDetailsByBankDetailsId")
    class getAccountDetailsByBankDetailsIdTest {

        @Test
        @DisplayName("Тест на успешный поиск информации об аккаунте по техническому идентификатору на реквизиты банка")
        void testGetAccountDetailsByBankDetailsIdPositive() throws Exception {
            accountDetailsDTO.setId(id);

            when(accountDetailsService.getAccountDetailsByBankDetailsId(accountDetailsDTO.getBankDetailsId())).thenReturn(accountDetailsDTO);

            MvcResult result = mockMvc.perform(get("/api/v1/account/bankDetailsId/{bankDetailsId}", accountDetailsDTO.getBankDetailsId()))
                    .andExpect(status().isOk())
                    .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();

            assertJsonResponseEquals(jsonResponse, accountDetailsDTO);
        }

        @Test
        @DisplayName(errorNotFound)
        void testGetAccountDetailsByBankDetailsIdNegativeNotFound() throws Exception {
            doThrow(new EntityNotFoundException(EntityNotFound)).when(accountDetailsService).getAccountDetailsByBankDetailsId(accountDetailsDTO.getBankDetailsId());

            mockMvc.perform(get("/api/v1/account/bankDetailsId/{bankDetailsId}", accountDetailsDTO.getBankDetailsId()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(EntityNotFound));
        }
    }

    @Nested
    @DisplayName("Тесты для метода getAllAccountDetails")
    class getAllAccountDetailsTest {

        @Test
        @DisplayName("Тест на успешное получение списка информации об аккаунтах с пагинацией")
        void testGetAllAccountDetailsPositive() throws Exception {
            accountDetailsDTO.setId(id);

            List<AccountDetailsDTO> accountDetailsList = new ArrayList<>();
            accountDetailsList.add(accountDetailsDTO);
            accountDetailsList.add(accountDetailsDTO);

            Page<AccountDetailsDTO> accountDetailsPage = new PageImpl<>(accountDetailsList);

            when(accountDetailsService.getAllAccountDetails(page, size)).thenReturn(accountDetailsPage);

            MvcResult result = mockMvc.perform(get("/api/v1/account/all")
                            .param("page", String.valueOf(page))
                            .param("size", String.valueOf(size)))
                    .andExpect(status().isOk())
                    .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();

            assertJsonResponseContentIndexEquals(jsonResponse, 0, accountDetailsDTO);

            assertJsonResponseContentIndexEquals(jsonResponse, 1, accountDetailsDTO);
        }

        @Test
        @DisplayName("Тест на ошибку сервера при получении списка информации об аккаунтах")
        void testGetAllAccountDetailsNegativeServerError() throws Exception {
            when(accountDetailsService.getAllAccountDetails(page, size))
                    .thenThrow(new RuntimeException("errorServerError"));

            mockMvc.perform(get("/api/v1/account/all")
                            .param("page", String.valueOf(page))
                            .param("size", String.valueOf(size)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.message").value("Произошла непредвиденная ошибка: errorServerError"));
        }
    }
}
