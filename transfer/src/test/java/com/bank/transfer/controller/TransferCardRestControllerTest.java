package com.bank.transfer.controller;

import com.bank.transfer.dto.CardTransferDTO;
import com.bank.transfer.model.CardTransfer;
import com.bank.transfer.mapper.CardTransferMapper;
import com.bank.transfer.serviceImpl.TransferCardServiceImpl;
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
public class TransferCardRestControllerTest {
    @Mock
    private TransferCardServiceImpl transferCardService;

    @Mock
    private CardTransferMapper cardTransferMapper;

    @InjectMocks
    private TransferCardRestController transferCardRestController;

    private MockMvc mockMvc;

    private CardTransfer cardTransfer;
    private CardTransferDTO cardTransferDTO;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transferCardRestController).build();

        cardTransfer = new CardTransfer(1L, new BigDecimal("100.00"), "Purpose", 2L);
        cardTransferDTO = new CardTransferDTO(1L, new BigDecimal("100.00"), "Purpose", 2L);
    }

    @Test
    void getCardTransfers_ShouldReturnListOfTransfers() throws Exception {
        Mockito.when(transferCardService.getAllCardTransfers()).thenReturn(List.of(cardTransfer));
        Mockito.when(cardTransferMapper.cardTransferToDTO(cardTransfer)).thenReturn(cardTransferDTO);

        mockMvc.perform(get("/v1/card")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].number").value(1))
                .andExpect(jsonPath("$[0].amount").value(100.00))
                .andExpect(jsonPath("$[0].purpose").value("Purpose"))
                .andExpect(jsonPath("$[0].accountDetailsId").value(2));

        Mockito.verifyNoMoreInteractions(transferCardService, cardTransferMapper);
    }

    @Test
    void getCardTransfer_ShouldReturnTransferById() throws Exception {
        Mockito.when(transferCardService.getCardTransferById(1L)).thenReturn(cardTransfer);
        Mockito.when(cardTransferMapper.cardTransferToDTO(cardTransfer)).thenReturn(cardTransferDTO);

        mockMvc.perform(get("/v1/card/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.purpose").value("Purpose"))
                .andExpect(jsonPath("$.accountDetailsId").value(2));

        Mockito.verifyNoMoreInteractions(transferCardService, cardTransferMapper);
    }

    @Test
    void createCardTransfer_ShouldReturnCreatedTransfer() throws Exception {
        Mockito.when(cardTransferMapper.dtoToCardTransfer(any())).thenReturn(cardTransfer);
        Mockito.when(transferCardService.addCardTransfer(any())).thenReturn(cardTransfer);
        Mockito.when(cardTransferMapper.cardTransferToDTO(any())).thenReturn(cardTransferDTO);

        mockMvc.perform(post("/v1/card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardTransferDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.purpose").value("Purpose"))
                .andExpect(jsonPath("$.accountDetailsId").value(2));

        Mockito.verifyNoMoreInteractions(transferCardService, cardTransferMapper);
    }

    @Test
    void createCardTransfer_ShouldReturnBadRequest_WhenDataIsInvalid() throws Exception {
        CardTransferDTO invalidDto = new CardTransferDTO(-2, new BigDecimal("0.00"), null, -5);

        mockMvc.perform(post("/v1/card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoMoreInteractions(transferCardService, cardTransferMapper);
    }

    @Test
    void updateCardTransfer_ShouldReturnUpdatedTransfer() throws Exception {
        Mockito.when(cardTransferMapper.dtoToCardTransfer(any())).thenReturn(cardTransfer);
        Mockito.when(transferCardService.updateCardTransfer(any(CardTransfer.class), anyLong())).thenReturn(cardTransfer);
        Mockito.when(cardTransferMapper.cardTransferToDTO(any())).thenReturn(cardTransferDTO);

        mockMvc.perform(put("/v1/card/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardTransferDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.purpose").value("Purpose"))
                .andExpect(jsonPath("$.accountDetailsId").value(2));

        Mockito.verifyNoMoreInteractions(transferCardService, cardTransferMapper);
    }

    @Test
    void updateCardTransfer_ShouldReturnBadRequest_WhenDataIsInvalid() throws Exception {
        CardTransferDTO invalidUpdateDto = new CardTransferDTO(-2, new BigDecimal("0.00"), null, -5);

        mockMvc.perform(post("/v1/card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateDto)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoMoreInteractions(transferCardService, cardTransferMapper);
    }

    @Test
    void deleteCardTransfer_ShouldReturnNoContent() throws Exception {
        doNothing().when(transferCardService).deleteCardTransfer(1L);

        mockMvc.perform(delete("/v1/card/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verifyNoMoreInteractions(transferCardService, cardTransferMapper);
    }
}
