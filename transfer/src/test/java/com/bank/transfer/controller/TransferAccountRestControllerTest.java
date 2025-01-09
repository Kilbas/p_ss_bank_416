package com.bank.transfer.controller;

import com.bank.transfer.dto.AccountTransferDTO;
import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.mapper.AccountTransferMapper;
import com.bank.transfer.serviceImpl.TransferAccountServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = TransferAccountRestController.class)
public class TransferAccountRestControllerTest {
    @MockBean
    private TransferAccountServiceImpl transferAccountService;

    @MockBean
    private AccountTransferMapper accountTransferMapper;

    @Autowired
    private MockMvc mockMvc;

    private AccountTransfer accountTransfer;
    private AccountTransferDTO accountTransferDTO;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        accountTransfer = new AccountTransfer(1L, new BigDecimal("100.00"), "Purpose", 2L);
        accountTransferDTO = new AccountTransferDTO(1L, new BigDecimal("100.00"), "Purpose", 2L);
    }

    @Nested
    @DisplayName("Тесты получения AccountTransfer (GET)")
    class GetAccountTransfers {

        @Test
        @DisplayName("Успешное получение всех AccountTransfer")
        void getAccountTransfers_ShouldReturnListOfTransfers() throws Exception {
            Mockito.when(transferAccountService.getAllAccountTransfers()).thenReturn(List.of(accountTransfer));
            Mockito.when(accountTransferMapper.accountTransferToDTO(accountTransfer)).thenReturn(accountTransferDTO);

            mockMvc.perform(get("/v1/account")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].number").value(1))
                    .andExpect(jsonPath("$[0].amount").value(100.00))
                    .andExpect(jsonPath("$[0].purpose").value("Purpose"))
                    .andExpect(jsonPath("$[0].accountDetailsId").value(2));
        }

        @Test
        @DisplayName("Успешное получение AccountTransfer по ID")
        void getAccountTransfer_ShouldReturnTransferById() throws Exception {
            Mockito.when(transferAccountService.getAccountTransferById(1L)).thenReturn(accountTransfer);
            Mockito.when(accountTransferMapper.accountTransferToDTO(accountTransfer)).thenReturn(accountTransferDTO);

            mockMvc.perform(get("/v1/account/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.number").value(1))
                    .andExpect(jsonPath("$.amount").value(100.00))
                    .andExpect(jsonPath("$.purpose").value("Purpose"))
                    .andExpect(jsonPath("$.accountDetailsId").value(2));
        }
    }

    @Nested
    @DisplayName("Тесты создания AccountTransfer (POST)")
    class CreateAccountTransfer {

        @Test
        @DisplayName("Успешное добавление AccountTransfer")
        void createAccountTransfer_ShouldReturnCreatedTransfer() throws Exception {
            Mockito.when(accountTransferMapper.dtoToAccountTransfer(any())).thenReturn(accountTransfer);
            Mockito.when(transferAccountService.addAccountTransfer(any())).thenReturn(accountTransfer);
            Mockito.when(accountTransferMapper.accountTransferToDTO(any())).thenReturn(accountTransferDTO);

            mockMvc.perform(post("/v1/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(accountTransferDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.number").value(1))
                    .andExpect(jsonPath("$.amount").value(100.00))
                    .andExpect(jsonPath("$.purpose").value("Purpose"))
                    .andExpect(jsonPath("$.accountDetailsId").value(2));
        }

        @Test
        @DisplayName("Попытка добавления AccountTransfer с невалидными данными")
        void createAccountTransfer_ShouldReturnBadRequest_WhenDataIsInvalid() throws Exception {
            AccountTransferDTO invalidDto = new AccountTransferDTO(-2, new BigDecimal("0.00"), null, -5);

            mockMvc.perform(post("/v1/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Тесты обновления AccountTransfer (PUT)")
    class UpdateAccountTransfer {

        @Test
        @DisplayName("Успешное обновление AccountTransfer")
        void updateAccountTransfer_ShouldReturnUpdatedTransfer() throws Exception {
            Mockito.when(accountTransferMapper.dtoToAccountTransfer(any())).thenReturn(accountTransfer);
            Mockito.when(transferAccountService.updateAccountTransfer(any(AccountTransfer.class), anyLong())).thenReturn(accountTransfer);
            Mockito.when(accountTransferMapper.accountTransferToDTO(any())).thenReturn(accountTransferDTO);

            mockMvc.perform(put("/v1/account/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(accountTransferDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.number").value(1))
                    .andExpect(jsonPath("$.amount").value(100.00));
        }

        @Test
        @DisplayName("Попытка обновления AccountTransfer с невалидными данными")
        void updateAccountTransfer_ShouldReturnBadRequest_WhenDataIsInvalid() throws Exception {
            AccountTransferDTO invalidUpdateDto = new AccountTransferDTO(-2, new BigDecimal("0.00"), null, -5);

            mockMvc.perform(put("/v1/account/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidUpdateDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Тесты удаления AccountTransfer (DELETE)")
    class DeleteAccountTransfer {

        @Test
        @DisplayName("Успешное удаление AccountTransfer")
        void deleteAccountTransfer_ShouldReturnNoContent() throws Exception {
            doNothing().when(transferAccountService).deleteAccountTransfer(1L);

            mockMvc.perform(delete("/v1/account/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }
    }
}
