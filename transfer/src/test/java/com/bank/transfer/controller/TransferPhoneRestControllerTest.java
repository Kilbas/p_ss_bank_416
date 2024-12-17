package com.bank.transfer.controller;

import com.bank.transfer.dto.PhoneTransferDTO;
import com.bank.transfer.model.PhoneTransfer;
import com.bank.transfer.mapper.PhoneTransferMapper;
import com.bank.transfer.serviceImpl.TransferPhoneServiceImpl;
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
public class TransferPhoneRestControllerTest {
    @Mock
    private TransferPhoneServiceImpl transferPhoneService;

    @Mock
    private PhoneTransferMapper phoneTransferMapper;

    @InjectMocks
    private TransferPhoneRestController transferPhoneRestController;

    private MockMvc mockMvc;

    private PhoneTransfer phoneTransfer;
    private PhoneTransferDTO phoneTransferDTO;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transferPhoneRestController).build();

        phoneTransfer = new PhoneTransfer(1L, new BigDecimal("100.00"), "Purpose", 2L);
        phoneTransferDTO = new PhoneTransferDTO(1L, new BigDecimal("100.00"), "Purpose", 2L);
    }

    @Test
    void getPhoneTransfers_ShouldReturnListOfTransfers() throws Exception {
        Mockito.when(transferPhoneService.getAllPhoneTransfers()).thenReturn(List.of(phoneTransfer));
        Mockito.when(phoneTransferMapper.phoneTransferToDTO(phoneTransfer)).thenReturn(phoneTransferDTO);

        mockMvc.perform(get("/v1/phone")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].number").value(1))
                .andExpect(jsonPath("$[0].amount").value(100.00))
                .andExpect(jsonPath("$[0].purpose").value("Purpose"))
                .andExpect(jsonPath("$[0].accountDetailsId").value(2));
    }

    @Test
    void getPhoneTransfer_ShouldReturnTransferById() throws Exception {
        Mockito.when(transferPhoneService.getPhoneTransferById(1L)).thenReturn(phoneTransfer);
        Mockito.when(phoneTransferMapper.phoneTransferToDTO(phoneTransfer)).thenReturn(phoneTransferDTO);

        mockMvc.perform(get("/v1/phone/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.purpose").value("Purpose"))
                .andExpect(jsonPath("$.accountDetailsId").value(2));
    }

    @Test
    void createPhoneTransfer_ShouldReturnCreatedTransfer() throws Exception {
        Mockito.when(phoneTransferMapper.dtoToPhoneTransfer(any())).thenReturn(phoneTransfer);
        Mockito.when(transferPhoneService.addPhoneTransfer(any())).thenReturn(phoneTransfer);
        Mockito.when(phoneTransferMapper.phoneTransferToDTO(any())).thenReturn(phoneTransferDTO);

        mockMvc.perform(post("/v1/phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phoneTransferDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.amount").value(100.00));
    }

    @Test
    void updatePhoneTransfer_ShouldReturnUpdatedTransfer() throws Exception {
        Mockito.when(phoneTransferMapper.dtoToPhoneTransfer(any())).thenReturn(phoneTransfer);
        Mockito.when(transferPhoneService.updatePhoneTransfer(any(PhoneTransfer.class), anyLong())).thenReturn(phoneTransfer);
        Mockito.when(phoneTransferMapper.phoneTransferToDTO(any())).thenReturn(phoneTransferDTO);

        mockMvc.perform(put("/v1/phone/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phoneTransferDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.amount").value(100.00));
    }

    @Test
    void deletePhoneTransfer_ShouldReturnNoContent() throws Exception {
        doNothing().when(transferPhoneService).deletePhoneTransfer(1L);

        mockMvc.perform(delete("/v1/phone/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
