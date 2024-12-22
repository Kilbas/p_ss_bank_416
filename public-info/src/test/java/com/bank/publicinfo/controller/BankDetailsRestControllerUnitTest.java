package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.BankDetailsDTO;
import com.bank.publicinfo.handler.ErrorResponse;
import com.bank.publicinfo.handler.GlobalExceptionHandler;
import com.bank.publicinfo.service.interfaceEntity.BankDetailsService;
import com.bank.publicinfo.util.CreateBankDetailsAndLicenseAndCertificate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class})
@DisplayName("Класс для тестирование BankDetailsRestController.")
class BankDetailsRestControllerUnitTest {

    private static final Long ID = 11L;

    @Mock
    private BankDetailsService bankDetailsService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(new BankDetailsRestController(bankDetailsService))
                .setControllerAdvice(new Object[]{new GlobalExceptionHandler()}).addFilter((request, response, chain) -> {
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        }).setCustomArgumentResolvers(new HandlerMethodArgumentResolver[]{new PageableHandlerMethodArgumentResolver()}).build();
    }

    @Test
    @DisplayName("Получить список всех BankDetails.")
    void getAllBankDetails() throws Exception {
        Pageable pageable = PageRequest.of(1, 2);
        List<BankDetailsDTO> all = List.of(CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO(),
                CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO());
        doReturn(all).when(bankDetailsService).getAllBankDetails(pageable);

        String content = mockMvc.perform(get("/bankDetails").queryParam("page",
                String.valueOf(pageable.getPageNumber())).queryParam("size", String.valueOf(pageable.getPageSize()))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<BankDetailsDTO> result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(all.size(), result.size());
        assertEquals(all.containsAll(result), result.containsAll(all));
        verify(bankDetailsService).getAllBankDetails(pageable);
    }

    @Test
    @DisplayName("Получение BankDetails. Успешный случай")
    void getBankDetailsSuccess() throws Exception {
        BankDetailsDTO dto = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        doReturn(dto).when(bankDetailsService).getBankDetails(dto.getId());

        ResultActions resultActions = mockMvc.perform(get("/bankDetails/" + dto.getId())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        String content = resultActions.andReturn().getResponse().getContentAsString();
        BankDetailsDTO result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertEquals(dto, result);
        verify(this.bankDetailsService).getBankDetails(dto.getId());
    }

    @Test
    @DisplayName("Получение BankDetails. Сущнось не найдена")
    void getBankDetailsNotFoundEntity() throws Exception {
        doThrow(new EntityNotFoundException(String.format("Реквизиты банка не найдены для ID: %s", ID))).when(bankDetailsService).getBankDetails(ID);

        ResultActions resultActions = mockMvc.perform(get("/bankDetails/" + ID)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
        String content = resultActions.andReturn().getResponse().getContentAsString();
        ErrorResponse response = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals("EntityNotFound", response.getCause());
        assertEquals(String.format("Реквизиты банка не найдены для ID: %s", ID), response.getMessage());
        verify(bankDetailsService).getBankDetails(ID);
    }

    @Test
    @DisplayName("Удаление BankDetails. Успешный случай.")
    void deleteBankDetailsSuccess() throws Exception {
        doNothing().when(bankDetailsService).deleteBankDetail(ID);

        mockMvc.perform(delete("/bankDetails/delete/" + ID)).andExpect(status().isNoContent());

        verify(bankDetailsService).deleteBankDetail(ID);
    }

    @Test
    @DisplayName("Создание BankDetails. Успешный случай.")
    void createBankDetailsSuccess() throws Exception {
        BankDetailsDTO bankDetailsDTO = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        doReturn(bankDetailsDTO).when(bankDetailsService).addBankDetails(bankDetailsDTO);

        String content = mockMvc.perform(post("/bankDetails/create")
                .content(objectMapper.writeValueAsString(bankDetailsDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        BankDetailsDTO result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals(bankDetailsDTO, result);
        verify(bankDetailsService).addBankDetails(bankDetailsDTO);
    }

    @Test
    @DisplayName("Создание BankDetails. NullPointerException.")
    void createBankDetailsWhenNullPointerException() throws Exception {
        BankDetailsDTO bankDetailsDTO = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        doThrow(new NullPointerException("text")).when(bankDetailsService).addBankDetails(bankDetailsDTO);

        String content = mockMvc.perform(post("/bankDetails/create")
                .content(objectMapper.writeValueAsString(bankDetailsDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()).andReturn().getResponse().getContentAsString();
        ErrorResponse result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals("Exception", result.getCause());
        assertEquals("text", result.getMessage());
        verify(bankDetailsService).addBankDetails(bankDetailsDTO);
    }

    @Test
    @DisplayName("Создание BankDetails. Ошибка валидации тела запроса.")
    void createBankDetailsNotValidRequest() throws Exception {
        BankDetailsDTO bankDetailsDTO = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        bankDetailsDTO.setBik(null);

        String content = mockMvc.perform(post("/bankDetails/create")
                .content(objectMapper.writeValueAsString(bankDetailsDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
        List<ErrorResponse> result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(1, result.size());
        assertEquals("Вы не указали bik", result.get(0).getMessage());
        assertEquals("bik", result.get(0).getCause());
    }

    @Test
    @DisplayName("Изменение BankDetails. Успешный случай.")
    void updateBankDetailsSuccess() throws Exception {
        BankDetailsDTO bankDetailsDTO = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        doReturn(bankDetailsDTO).when(bankDetailsService).updateBankDetails(bankDetailsDTO.getId(), bankDetailsDTO);

        String content = mockMvc.perform(patch("/bankDetails/update/" + bankDetailsDTO.getId())
                .content(objectMapper.writeValueAsString(bankDetailsDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        BankDetailsDTO result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals(bankDetailsDTO, result);
        verify(bankDetailsService).updateBankDetails(bankDetailsDTO.getId(), bankDetailsDTO);
    }

    @Test
    @DisplayName("Изменение BankDetails. DataAccessException.")
    void updateBankDetailsWhenDataAccessException() throws Exception {
        BankDetailsDTO bankDetailsDTO = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        doThrow(new DuplicateKeyException("text")).when(bankDetailsService).updateBankDetails(bankDetailsDTO.getId(), bankDetailsDTO);

        String content = mockMvc.perform(patch("/bankDetails/update/" + bankDetailsDTO.getId())
                .content(objectMapper.writeValueAsString(bankDetailsDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity()).andReturn().getResponse().getContentAsString();
        ErrorResponse result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals("DataAccessException", result.getCause());
        assertEquals("text", result.getMessage());
        verify(bankDetailsService).updateBankDetails(bankDetailsDTO.getId(), bankDetailsDTO);
    }
}