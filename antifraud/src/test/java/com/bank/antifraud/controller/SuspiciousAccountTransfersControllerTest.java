package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.service.SuspiciousAccountTransfersService;
import com.bank.antifraud.util.TestDataSuspiciousAccountTransfers;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(SuspiciousAccountTransfersController.class)
class SuspiciousAccountTransfersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SuspiciousAccountTransfersService service;

    private final SuspiciousAccountTransfersDTO dto = TestDataSuspiciousAccountTransfers.createSuspiciousAccountTransfersDTO();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Nested
    @DisplayName("Find By ID")
    class FindById {

        @Test
        @DisplayName("Should return DTO when found")
        void shouldReturnDtoWhenFound() throws Exception {
            when(service.findByIdAccountTransfer(anyLong())).thenReturn(dto);

            mockMvc.perform(get("/suspicious-account-transfers/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(dto.getId()))
                    .andExpect(jsonPath("$.accountTransferId").value(dto.getAccountTransferId()));
        }

        @Test
        @DisplayName("Should return 404 when not found")
        void shouldReturn404WhenNotFound() throws Exception {
            Mockito.when(service.findByIdAccountTransfer(anyLong()))
                    .thenThrow(new EntityNotFoundException("Запись с ID %d не найдена. Обновление невозможно."));

            mockMvc.perform(get("/suspicious-account-transfers/{id}", 1L))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Find All")
    class FindAll {

        @Test
        @DisplayName("Should return list of DTOs")
        void shouldReturnListOfDtos() throws Exception {
            when(service.findAllAccountTransfers()).thenReturn(List.of(dto));

            mockMvc.perform(get("/suspicious-account-transfers"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(dto.getId()));
        }

        @Test
        @DisplayName("Should return empty list")
        void shouldReturnEmptyList() throws Exception {
            when(service.findAllAccountTransfers()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/suspicious-account-transfers"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("Create")
    class Create {

        @Test
        @DisplayName("Should create and return DTO")
        void shouldCreateAndReturnDto() throws Exception {
            when(service.createNewAccountTransfer(any(SuspiciousAccountTransfersDTO.class))).thenReturn(dto);

            mockMvc.perform(post("/suspicious-account-transfers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(dto.getId()));
        }
    }

    @Nested
    @DisplayName("Update")
    class Update {

        @Test
        @DisplayName("Should update and return DTO")
        void shouldUpdateAndReturnDto() throws Exception {
            when(service.updateAccountTransfer(anyLong(), any(SuspiciousAccountTransfersDTO.class))).thenReturn(dto);

            mockMvc.perform(put("/suspicious-account-transfers/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(dto.getId()));
        }
    }

    @Nested
    @DisplayName("Delete")
    class Delete {

        @Test
        @DisplayName("Should delete successfully")
        void shouldDeleteSuccessfully() throws Exception {
            doNothing().when(service).deleteAccountTransfer(anyLong());

            mockMvc.perform(delete("/suspicious-account-transfers/{id}", 1L))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Find Transfers By Reason")
    class FindTransfersByReason {

        @Test
        @DisplayName("Should return matching transfers")
        void shouldReturnMatchingTransfers() throws Exception {
            when(service.findTransfersByReason(anyString())).thenReturn(List.of(dto));

            mockMvc.perform(get("/suspicious-account-transfers/reason")
                            .param("reason", "Fraud"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(dto.getId()))
                    .andExpect(jsonPath("$[0].suspiciousReason").value("Large amount transfer"));
        }

        @Test
        @DisplayName("Should return no content when no matches")
        void shouldReturnNoContentWhenNoMatches() throws Exception {
            when(service.findTransfersByReason(anyString())).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/suspicious-account-transfers/reason")
                            .param("reason", "Nonexistent"))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Find Blocked Transfers")
    class FindBlockedTransfers {

        @Test
        @DisplayName("Should return blocked transfers")
        void shouldReturnBlockedTransfers() throws Exception {
            when(service.findBlockedTransfers()).thenReturn(List.of(dto));

            mockMvc.perform(get("/suspicious-account-transfers/blocked"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(dto.getId()))
                    .andExpect(jsonPath("$[0].isBlocked").value(true));
        }

        @Test
        @DisplayName("Should return empty list when no blocked transfers")
        void shouldReturnEmptyListWhenNoBlockedTransfers() throws Exception {
            when(service.findBlockedTransfers()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/suspicious-account-transfers/blocked"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("Find Suspicious Transfers")
    class FindSuspiciousTransfers {

        @Test
        @DisplayName("Should return suspicious transfers")
        void shouldReturnSuspiciousTransfers() throws Exception {
            when(service.findSuspiciousTransfers()).thenReturn(List.of(dto));

            mockMvc.perform(get("/suspicious-account-transfers/suspicious"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(dto.getId()))
                    .andExpect(jsonPath("$[0].isSuspicious").value(true));
        }

        @Test
        @DisplayName("Should return empty list when no suspicious transfers")
        void shouldReturnEmptyListWhenNoSuspiciousTransfers() throws Exception {
            when(service.findSuspiciousTransfers()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/suspicious-account-transfers/suspicious"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }
}
