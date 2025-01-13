package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;
import com.bank.antifraud.service.SuspiciousPhoneTransfersService;
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
@WebMvcTest(SuspiciousPhoneTransfersController.class)
class SuspiciousPhoneTransfersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SuspiciousPhoneTransfersService service;

    private final SuspiciousPhoneTransferDTO dto = SuspiciousPhoneTransferDTO.builder()
            .id(1L)
            .phoneTransferId(100L)
            .isBlocked(true)
            .isSuspicious(true)
            .blockedReason("Fraud detected")
            .suspiciousReason("Large amount transfer")
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Nested
    @DisplayName("Find By ID")
    class FindById {

        @Test
        @DisplayName("Should return DTO when found")
        void shouldReturnDtoWhenFound() throws Exception {
            when(service.findByIdPhoneTransfers(anyLong())).thenReturn(dto);

            mockMvc.perform(get("/suspicious-phone-transfers/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(dto.getId()))
                    .andExpect(jsonPath("$.phoneTransferId").value(dto.getPhoneTransferId()));
        }

        @Test
        @DisplayName("Should return 404 when not found")
        void shouldReturn404WhenNotFound() throws Exception {
            Mockito.when(service.findByIdPhoneTransfers(anyLong()))
                    .thenThrow(new EntityNotFoundException("Not found"));

            mockMvc.perform(get("/suspicious-phone-transfers/{id}", 1L))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Find All")
    class FindAll {

        @Test
        @DisplayName("Should return list of DTOs")
        void shouldReturnListOfDtos() throws Exception {
            when(service.findAllPhoneTransfers()).thenReturn(List.of(dto));

            mockMvc.perform(get("/suspicious-phone-transfers"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(dto.getId()));
        }

        @Test
        @DisplayName("Should return empty list")
        void shouldReturnEmptyList() throws Exception {
            when(service.findAllPhoneTransfers()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/suspicious-phone-transfers"))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Create")
    class Create {

        @Test
        @DisplayName("Should create and return DTO")
        void shouldCreateAndReturnDto() throws Exception {
            when(service.createNewPhoneTransfers(any(SuspiciousPhoneTransferDTO.class))).thenReturn(dto);

            mockMvc.perform(post("/suspicious-phone-transfers")
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
            when(service.updatePhoneTransfers(anyLong(), any(SuspiciousPhoneTransferDTO.class))).thenReturn(dto);

            mockMvc.perform(put("/suspicious-phone-transfers/{id}", 1L)
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
            doNothing().when(service).deletePhoneTransfers(anyLong());

            mockMvc.perform(delete("/suspicious-phone-transfers/{id}", 1L))
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

            mockMvc.perform(get("/suspicious-phone-transfers/reason/{reason}", "Fraud"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(dto.getId()))
                    .andExpect(jsonPath("$[0].suspiciousReason").value("Large amount transfer"));
        }

        @Test
        @DisplayName("Should return no content when no matches")
        void shouldReturnNoContentWhenNoMatches() throws Exception {
            when(service.findTransfersByReason(anyString())).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/suspicious-phone-transfers/reason/{reason}", "Nonexistent"))
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

            mockMvc.perform(get("/suspicious-phone-transfers/blocked"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(dto.getId()))
                    .andExpect(jsonPath("$[0].isBlocked").value(true));
        }

        @Test
        @DisplayName("Should return no content when no blocked transfers")
        void shouldReturnNoContentWhenNoBlockedTransfers() throws Exception {
            when(service.findBlockedTransfers()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/suspicious-phone-transfers/blocked"))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Find Suspicious Transfers")
    class FindSuspiciousTransfers {

        @Test
        @DisplayName("Should return suspicious transfers")
        void shouldReturnSuspiciousTransfers() throws Exception {
            when(service.findSuspiciousTransfers()).thenReturn(List.of(dto));

            mockMvc.perform(get("/suspicious-phone-transfers/suspicious"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(dto.getId()))
                    .andExpect(jsonPath("$[0].isSuspicious").value(true));
        }

        @Test
        @DisplayName("Should return no content when no suspicious transfers")
        void shouldReturnNoContentWhenNoSuspiciousTransfers() throws Exception {
            when(service.findSuspiciousTransfers()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/suspicious-phone-transfers/suspicious"))
                    .andExpect(status().isNoContent());
        }
    }
}
