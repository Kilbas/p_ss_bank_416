package com.bank.history.controllers;

import com.bank.history.dto.HistoryDTO;
import com.bank.history.services.HistoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class HistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoryService historyService;

    private static final HistoryDTO historyDTO = new HistoryDTO(1L, 2L,
            3L, 4L, 5L, 6L, 7L);

    @Test
    @DisplayName("Тест сохранения истории с валидными данными")
    void testSaveHistoryValid() throws Exception {
        String validJson = """
                {
                    "transferAuditId": 1,
                    "profileAuditId": 2,
                    "accountAuditId": 3,
                    "antiFraudAuditId": 4,
                    "publicBankInfoAuditId": 5,
                    "authorizationAuditId": 6
                }
                """;

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Тест сохранения истории с невалидными данными")
    void testSaveHistoryInvalid() throws Exception {
        String invalidJson = """
                {
                    "transferAuditId": 0,
                    "profileAuditId": -1,
                    "accountAuditId": null,
                    "antiFraudAuditId": 4,
                    "publicBankInfoAuditId": 5,
                    "authorizationAuditId": 6
                }
                """;

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("значение не может быть меньше 1")));
    }

    @Test
    @DisplayName("Тест получения истории по ID")
    void testGetHistoryById() throws Exception {
        when(historyService.findById(1L)).thenReturn(historyDTO);

        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.transferAuditId").value(2));
    }

    @Test
    @DisplayName("Тест получения истории по несуществующему ID")
    void testGetHistoryByIdNotFound() throws Exception {
        when(historyService.findById(anyLong()))
                .thenThrow(new EntityNotFoundException("History not found"));

        mockMvc.perform(get("/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("History not found"));
    }

    @Test
    @DisplayName("Тест получения всех историй")
    void testGetAllHistory() throws Exception {
        when(historyService.findAll(any())).thenReturn(new PageImpl<>(Collections.singletonList(historyDTO)));

        mockMvc.perform(get("/")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].transferAuditId").value(2));
    }

    @Test
    @DisplayName("Тест обновления истории")
    void testUpdateHistory() throws Exception {
        String updateJson = """
                {
                    "transferAuditId": 10
                }
                """;

        mockMvc.perform(patch("/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест удаления истории по ID")
    void testDeleteHistoryById() throws Exception {
        mockMvc.perform(delete("/1"))
                .andExpect(status().isOk());
    }
}
