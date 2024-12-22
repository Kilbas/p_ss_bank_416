package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.CertificateDTO;
import com.bank.publicinfo.handler.ErrorResponse;
import com.bank.publicinfo.handler.GlobalExceptionHandler;
import com.bank.publicinfo.service.interfaceEntity.CertificateService;
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
@DisplayName("Класс для тестирование CertificateRestController.")
class CertificateRestControllerUnitTest {

    private static final Long ID = 11L;

    @Mock
    private CertificateService certificateService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(new CertificateRestController(certificateService))
                .setControllerAdvice(new Object[]{new GlobalExceptionHandler()}).addFilter((request, response, chain) -> {
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        }).setCustomArgumentResolvers(new HandlerMethodArgumentResolver[]{new PageableHandlerMethodArgumentResolver()}).build();
    }

    @Test
    @DisplayName("Получить список всех Certificate.")
    void getAllCertificate() throws Exception {
        Pageable pageable = PageRequest.of(1, 2);
        List<CertificateDTO> all = List.of(CreateBankDetailsAndLicenseAndCertificate.createCertificateDTO(),
                CreateBankDetailsAndLicenseAndCertificate.createCertificateDTO());
        doReturn(all).when(certificateService).getAllCertificates(pageable);

        String content = mockMvc.perform(get("/certificate").queryParam("page",
                String.valueOf(pageable.getPageNumber())).queryParam("size", String.valueOf(pageable.getPageSize()))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<CertificateDTO> result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(all.size(), result.size());
        assertEquals(all.containsAll(result), result.containsAll(all));
        verify(certificateService).getAllCertificates(pageable);
    }

    @Test
    @DisplayName("Получение Certificate. Успешный случай")
    void getCertificateSuccess() throws Exception {
        CertificateDTO dto = CreateBankDetailsAndLicenseAndCertificate.createCertificateDTO();
        doReturn(dto).when(certificateService).getCertificate(dto.getId());

        ResultActions resultActions = mockMvc.perform(get("/certificate/" + dto.getId())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        String content = resultActions.andReturn().getResponse().getContentAsString();
        CertificateDTO result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertEquals(dto.getId(), result.getId());
        verify(certificateService).getCertificate(dto.getId());
    }

    @Test
    @DisplayName("Получение Certificate. Сущнось не найдена")
    void getCertificateNotFoundEntity() throws Exception {
        doThrow(new EntityNotFoundException(String.format("Сертификат не найден с id %s", ID))).when(certificateService).getCertificate(ID);

        ResultActions resultActions = mockMvc.perform(get("/certificate/" + ID)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
        String content = resultActions.andReturn().getResponse().getContentAsString();
        ErrorResponse response = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals("EntityNotFound", response.getCause());
        assertEquals(String.format("Сертификат не найден с id %s", ID), response.getMessage());
        verify(certificateService).getCertificate(ID);
    }

    @Test
    @DisplayName("Удаление Certificate. Успешный случай.")
    void deleteCertificateSuccess() throws Exception {
        doNothing().when(certificateService).deleteCertificate(ID);

        mockMvc.perform(delete("/certificate/delete/" + ID)).andExpect(status().isNoContent());

        verify(certificateService).deleteCertificate(ID);
    }

    @Test
    @DisplayName("Создание Certificate. Успешный случай.")
    void createCertificateSuccess() throws Exception {
        CertificateDTO certificateDTO = CreateBankDetailsAndLicenseAndCertificate.createCertificateDTO();
        doReturn(certificateDTO).when(certificateService).addCertificate(certificateDTO);

        String content = mockMvc.perform(post("/certificate/create")
                .content(objectMapper.writeValueAsString(certificateDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        CertificateDTO result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals(certificateDTO, result);
        verify(certificateService).addCertificate(certificateDTO);
    }

    @Test
    @DisplayName("Создание Certificate. NullPointerException.")
    void createCertificateWhenNullPointerException() throws Exception {
        CertificateDTO certificateDTO = CreateBankDetailsAndLicenseAndCertificate.createCertificateDTO();
        doThrow(new NullPointerException("text")).when(certificateService).addCertificate(certificateDTO);

        String content = mockMvc.perform(post("/certificate/create").content(objectMapper.writeValueAsString(certificateDTO))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError()).andReturn().getResponse().getContentAsString();
        ErrorResponse result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals("Exception", result.getCause());
        assertEquals("text", result.getMessage());
        verify(certificateService).addCertificate(certificateDTO);
    }

    @Test
    @DisplayName("Создание Certificate. Ошибка валидации тела запроса.")
    void createBranchNotValidRequest() throws Exception {
        CertificateDTO certificateDTO = CreateBankDetailsAndLicenseAndCertificate.createCertificateDTO();
        certificateDTO.setPhoto(null);

        String content = mockMvc.perform(post("/certificate/create").content(objectMapper.writeValueAsString(certificateDTO))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
        List<ErrorResponse> result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(1, result.size());
        assertEquals("Вы не указали photo", result.get(0).getMessage());
        assertEquals("photo", result.get(0).getCause());
    }

    @Test
    @DisplayName("Изменение Certificate. Успешный случай.")
    void updateCertificateSuccess() throws Exception {
        CertificateDTO certificateDTO = CreateBankDetailsAndLicenseAndCertificate.createCertificateDTO();
        doReturn(certificateDTO).when(certificateService).updateCertificate(certificateDTO.getId(), certificateDTO);

        String content = mockMvc.perform(patch("/certificate/update/" + certificateDTO.getId())
                .content(objectMapper.writeValueAsString(certificateDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        CertificateDTO result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals(certificateDTO, result);
        verify(certificateService).updateCertificate(certificateDTO.getId(), certificateDTO);
    }

    @Test
    @DisplayName("Изменение Certificate. DataAccessException.")
    void updateCertificateWhenDataAccessException() throws Exception {
        CertificateDTO certificateDTO = CreateBankDetailsAndLicenseAndCertificate.createCertificateDTO();
        doThrow(new DuplicateKeyException("text")).when(certificateService).updateCertificate(certificateDTO.getId(), certificateDTO);

        String content = mockMvc.perform(patch("/certificate/update/" + certificateDTO.getId())
                .content(objectMapper.writeValueAsString(certificateDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity()).andReturn().getResponse().getContentAsString();
        ErrorResponse result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals("DataAccessException", result.getCause());
        assertEquals("text", result.getMessage());
        verify(certificateService).updateCertificate(certificateDTO.getId(), certificateDTO);
    }
}