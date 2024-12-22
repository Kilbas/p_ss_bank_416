package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.LicenseDTO;
import com.bank.publicinfo.handler.ErrorResponse;
import com.bank.publicinfo.handler.GlobalExceptionHandler;
import com.bank.publicinfo.service.interfaceEntity.LicenseService;
import com.bank.publicinfo.util.CreateBankDetailsAndLicenseAndCertificate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import javax.persistence.EntityNotFoundException;
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
@DisplayName("Класс для тестирование LicenseRestController.")
class LicenseRestControllerUnitTest {
    private static final Long ID = 11L;
    @Mock
    private LicenseService licenseService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    LicenseRestControllerUnitTest() {
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(new LicenseRestController(this.licenseService))
                .setControllerAdvice(new Object[]{new GlobalExceptionHandler()}).addFilter((request, response, chain) -> {
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        }).setCustomArgumentResolvers(new HandlerMethodArgumentResolver[]{new PageableHandlerMethodArgumentResolver()}).build();
    }

    @Test
    @DisplayName("Получить список всех Licenses.")
    void getAllLicenses() throws Exception {
        Pageable pageable = PageRequest.of(1, 2);
        List<LicenseDTO> all = List.of(CreateBankDetailsAndLicenseAndCertificate.createLicenseDTO(),
                CreateBankDetailsAndLicenseAndCertificate.createLicenseDTO());
        doReturn(all).when(licenseService).getAllLicenses(pageable);

        String content = mockMvc.perform(get("/license").queryParam("page", String.valueOf(pageable.getPageNumber()))
                .queryParam("size", String.valueOf(pageable.getPageSize())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<LicenseDTO> result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(all.size(), result.size());
        assertEquals(all.containsAll(result), result.containsAll(all));
        verify(licenseService).getAllLicenses(pageable);
    }

    @Test
    @DisplayName("Получение License. Успешный случай")
    void getLicenseSuccess() throws Exception {
        LicenseDTO dto = CreateBankDetailsAndLicenseAndCertificate.createLicenseDTO();
        doReturn(dto).when(licenseService).getLicense(dto.getId());

        ResultActions resultActions = mockMvc.perform(get("/license/" + dto.getId())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        String content = resultActions.andReturn().getResponse().getContentAsString();
        LicenseDTO result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertEquals(dto, result);
        verify(licenseService).getLicense(dto.getId());
    }

    @Test
    @DisplayName("Получение License. Сущнось не найдена")
    void getLicenseNotFoundEntity() throws Exception {
        doThrow(new EntityNotFoundException(String.format("Лицензия не найден с id %s", ID))).when(licenseService).getLicense(ID);

        ResultActions resultActions = mockMvc.perform(get("/license/" + ID).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        String content = resultActions.andReturn().getResponse().getContentAsString();
        ErrorResponse response = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals("EntityNotFound", response.getCause());
        assertEquals(String.format("Лицензия не найден с id %s", ID), response.getMessage());
        verify(licenseService).getLicense(ID);
    }

    @Test
    @DisplayName("Удаление License. Успешный случай.")
    void deleteLicenseSuccess() throws Exception {
        doNothing().when(licenseService).deleteLicense(ID);

        this.mockMvc.perform(delete("/license/delete/" + ID)).andExpect(status().isNoContent());

        verify(licenseService).deleteLicense(ID);
    }

    @Test
    @DisplayName("Создание License. Успешный случай.")
    void createLicenseSuccess() throws Exception {
        LicenseDTO licenseDTO = CreateBankDetailsAndLicenseAndCertificate.createLicenseDTO();
        doReturn(licenseDTO).when(licenseService).addLicense(licenseDTO);

        String content = mockMvc.perform(post("/license/create").content(objectMapper.writeValueAsString(licenseDTO))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        LicenseDTO result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals(licenseDTO, result);
        verify(licenseService).addLicense(licenseDTO);
    }

    @Test
    @DisplayName("Создание License. NullPointerException.")
    void createLicenseWhenNullPointerException() throws Exception {
        LicenseDTO licenseDTO = CreateBankDetailsAndLicenseAndCertificate.createLicenseDTO();
        doThrow(new NullPointerException("text")).when(licenseService).addLicense(licenseDTO);

        String content = mockMvc.perform(post("/license/create").content(objectMapper.writeValueAsString(licenseDTO))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError()).andReturn().getResponse().getContentAsString();
        ErrorResponse result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals("Exception", result.getCause());
        assertEquals("text", result.getMessage());
        verify(licenseService).addLicense(licenseDTO);
    }

    @Test
    @DisplayName("Создание License. Ошибка валидации тела запроса.")
    void createLicenseNotValidRequest() throws Exception {
        LicenseDTO licenseDTO = CreateBankDetailsAndLicenseAndCertificate.createLicenseDTO();
        licenseDTO.setPhoto(null);

        String content = mockMvc.perform(post("/license/create").content(objectMapper.writeValueAsString(licenseDTO))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
        List<ErrorResponse> result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(1, result.size());
        assertEquals("Вы не указали photo", result.get(0).getMessage());
        assertEquals("photo", result.get(0).getCause());
    }

    @Test
    @DisplayName("Изменение License. Успешный случай.")
    void updateLicenseSuccess() throws Exception {
        LicenseDTO licenseDTO = CreateBankDetailsAndLicenseAndCertificate.createLicenseDTO();
        doReturn(licenseDTO).when(licenseService).updateLicense(licenseDTO.getId(), licenseDTO);

        String content = mockMvc.perform(patch("/license/update/" + licenseDTO.getId())
                .content(objectMapper.writeValueAsString(licenseDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        LicenseDTO result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals(licenseDTO, result);
        verify(licenseService).updateLicense(licenseDTO.getId(), licenseDTO);
    }

    @Test
    @DisplayName("Изменение License. DataAccessException.")
    void updateLicenseWhenDataAccessException() throws Exception {
        LicenseDTO licenseDTO = CreateBankDetailsAndLicenseAndCertificate.createLicenseDTO();
        doThrow(new DuplicateKeyException("text")).when(licenseService).updateLicense(licenseDTO.getId(), licenseDTO);

        String content = mockMvc.perform(patch("/license/update/" + licenseDTO.getId())
                .content(objectMapper.writeValueAsString(licenseDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity()).andReturn().getResponse().getContentAsString();
        ErrorResponse result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals("DataAccessException", result.getCause());
        assertEquals("text", result.getMessage());
        verify(licenseService).updateLicense(licenseDTO.getId(), licenseDTO);
    }
}