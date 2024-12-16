package com.bank.transfer.controller;

import com.bank.transfer.dto.AccountTransferDTO;
import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.mapper.AccountTransferMapper;
import com.bank.transfer.service.TransferAccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TransferAccountRestController.class)
public class TransferAccountRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferAccountService transferAccountService;

    @MockBean
    private AccountTransferMapper accountTransferMapper;

    @Test
    void getAccountTransfers_shouldReturnListOfTransfers() throws Exception {
        // Arrange
        AccountTransfer transfer1 = new AccountTransfer(1L, new BigDecimal("100.00"), "Purpose 1", 1L);
        AccountTransfer transfer2 = new AccountTransfer(2L, new BigDecimal("200.00"), "Purpose 2", 2L);

        List<AccountTransfer> transfers = Arrays.asList(transfer1, transfer2);

        AccountTransferDTO transferDTO1 = new AccountTransferDTO(1L, new BigDecimal("100.00"), "Purpose 1", 1L);
        AccountTransferDTO transferDTO2 = new AccountTransferDTO(2L, new BigDecimal("200.00"), "Purpose 2", 2L);

        List<AccountTransferDTO> transferDTOs = Arrays.asList(transferDTO1, transferDTO2);

        Mockito.when(transferAccountService.getAllAccountTransfers()).thenReturn(transfers);
        Mockito.when(accountTransferMapper.accountTransferToDTO(transfer1)).thenReturn(transferDTO1);
        Mockito.when(accountTransferMapper.accountTransferToDTO(transfer2)).thenReturn(transferDTO2);

        mockMvc.perform(get("/v1/account"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].number").value(1L))
                .andExpect(jsonPath("$[0].amount").value(100.00))
                .andExpect(jsonPath("$[0].purpose").value("Purpose 1"))
                .andExpect(jsonPath("$[1].number").value(2L))
                .andExpect(jsonPath("$[1].amount").value(200.00))
                .andExpect(jsonPath("$[1].purpose").value("Purpose 2"));
    }
}
