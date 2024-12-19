package com.bank.transfer.controller;

import com.bank.transfer.dto.AccountTransferDTO;
import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.mapper.AccountTransferMapper;
import com.bank.transfer.serviceImpl.TransferAccountServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
public class TransferAccountRestControllerTest {
    @Mock
    private TransferAccountServiceImpl transferAccountService;

    @Mock
    private AccountTransferMapper accountTransferMapper;

    @InjectMocks
    private TransferAccountRestController transferAccountRestController;

    private MockMvc mockMvc;

    private AccountTransfer accountTransfer;
    private AccountTransferDTO accountTransferDTO;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transferAccountRestController).build();

        accountTransfer = new AccountTransfer(1L, new BigDecimal("100.00"), "Purpose", 2L);
        accountTransferDTO = new AccountTransferDTO(1L, new BigDecimal("100.00"), "Purpose", 2L);
    }

    @Test
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

    @Test
    void createAccountTransfer_ShouldReturnCreatedTransfer() throws Exception {
        Mockito.when(accountTransferMapper.dtoToAccountTransfer(any())).thenReturn(accountTransfer);
        Mockito.when(transferAccountService.addAccountTransfer(any())).thenReturn(accountTransfer);
        Mockito.when(accountTransferMapper.accountTransferToDTO(any())).thenReturn(accountTransferDTO);

        mockMvc.perform(post("/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountTransferDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.amount").value(100.00));
    }

    @Test
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
    void deleteAccountTransfer_ShouldReturnNoContent() throws Exception {
        doNothing().when(transferAccountService).deleteAccountTransfer(1L);

        mockMvc.perform(delete("/v1/account/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
