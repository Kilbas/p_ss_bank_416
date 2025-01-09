package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.AtmDTO;
import com.bank.publicinfo.handler.ErrorResponse;
import com.bank.publicinfo.handler.GlobalExceptionHandler;
import com.bank.publicinfo.service.interfaceEntity.AtmService;
import com.bank.publicinfo.util.CreateAtmAndBranch;
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
@DisplayName("Класс для тестирование AtmRestController.")
class AtmRestControllerUnitTest {

    private static final Long ID = 11L;

    @Mock
    private AtmService atmService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(new AtmRestController(atmService)).
                setControllerAdvice(new Object[]{new GlobalExceptionHandler()}).addFilter((request, response, chain) -> {
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        }).setCustomArgumentResolvers(new HandlerMethodArgumentResolver[]{new PageableHandlerMethodArgumentResolver()}).build();
    }

    @Test
    @DisplayName("Получить список всех Atm.")
    void getAllAtm() throws Exception {
        Pageable pageable = PageRequest.of(1, 2);
        List<AtmDTO> all = List.of(CreateAtmAndBranch.createAtmDTO(), CreateAtmAndBranch.createAtmDTO());
        doReturn(all).when(atmService).getAllAtms(pageable);

        String content = mockMvc.perform(get("/atm").queryParam("page", String.valueOf(pageable.getPageNumber()))
                .queryParam("size", String.valueOf(pageable.getPageSize())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<AtmDTO> result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(all.size(), result.size());
        assertEquals(all.containsAll(result), result.containsAll(all));
        verify(atmService).getAllAtms(pageable);
    }

    @Test
    @DisplayName("Получение Atm. Успешный случай")
    void getAtmSuccess() throws Exception {
        AtmDTO dto = CreateAtmAndBranch.createAtmDTO();
        doReturn(dto).when(atmService).getAtm(dto.getId());

        ResultActions resultActions = mockMvc.perform(get("/atm/" + dto.getId())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        String content = resultActions.andReturn().getResponse().getContentAsString();
        AtmDTO result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertEquals(dto, result);
        verify(atmService).getAtm(dto.getId());
    }

    @Test
    @DisplayName("Получение Atm. Сущнось не найдена")
    void getAtmNotFoundEntity() throws Exception {
        doThrow(new EntityNotFoundException(String.format("Банкомат с указанным id не найден в БД %s", ID))).when(atmService).getAtm(ID);

        ResultActions resultActions = mockMvc.perform(get("/atm/" + ID).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        String content = resultActions.andReturn().getResponse().getContentAsString();
        ErrorResponse response = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals("EntityNotFound", response.getCause());
        assertEquals(String.format("Банкомат с указанным id не найден в БД %s", ID), response.getMessage());
        verify(atmService).getAtm(ID);
    }

    @Test
    @DisplayName("Удаление Atm. Успешный случай.")
    void deleteAtmSuccess() throws Exception {
        doNothing().when(atmService).deleteAtm(ID);

        mockMvc.perform(delete("/atm/delete/" + ID)).andExpect(status().isNoContent());

        verify(atmService).deleteAtm(ID);
    }

    @Test
    @DisplayName("Создание Atm. Успешный случай.")
    void createAtmSuccess() throws Exception {
        AtmDTO atmDTO = CreateAtmAndBranch.createAtmDTO();
        doReturn(atmDTO).when(atmService).addAtm(atmDTO);

        String content = mockMvc.perform(post("/atm/create").content(objectMapper.writeValueAsString(atmDTO))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        AtmDTO result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals(atmDTO, result);
        verify(atmService).addAtm(atmDTO);
    }

    @Test
    @DisplayName("Создание Atm. NullPointerException.")
    void createAtmWhenNullPointerException() throws Exception {
        AtmDTO atmDTO = CreateAtmAndBranch.createAtmDTO();
        doThrow(new NullPointerException("text")).when(atmService).addAtm(atmDTO);

        String content = mockMvc.perform(post("/atm/create")
                .content(objectMapper.writeValueAsString(atmDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()).andReturn().getResponse().getContentAsString();
        ErrorResponse result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals("Exception", result.getCause());
        assertEquals("text", result.getMessage());
        verify(atmService).addAtm(atmDTO);
    }

    @Test
    @DisplayName("Создание Atm. Ошибка валидации тела запроса.")
    void createAtmNotValidRequest() throws Exception {
        AtmDTO atmDTO = CreateAtmAndBranch.createAtmDTO();
        atmDTO.setAddress(null);

        String content = mockMvc.perform(post("/atm/create")
                .content(objectMapper.writeValueAsString(atmDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
        List<ErrorResponse> result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(1, result.size());
        assertEquals("Вы не указали address", result.get(0).getMessage());
        assertEquals("address", result.get(0).getCause());
    }

    @Test
    @DisplayName("Изменение Atm. Успешный случай.")
    void updateAtmSuccess() throws Exception {
        AtmDTO atmDTO = CreateAtmAndBranch.createAtmDTO();
        doReturn(atmDTO).when(atmService).updateAtm(atmDTO.getId(), atmDTO);

        String content = mockMvc.perform(patch("/atm/update/" + atmDTO.getId())
                .content(objectMapper.writeValueAsString(atmDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        AtmDTO result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals(atmDTO, result);
        verify(atmService).updateAtm(atmDTO.getId(), atmDTO);
    }

    @Test
    @DisplayName("Изменение Atm. DataAccessException.")
    void updateAtmWhenDataAccessException() throws Exception {
        AtmDTO atmDTO = CreateAtmAndBranch.createAtmDTO();
        doThrow(new DuplicateKeyException("text")).when(atmService).updateAtm(atmDTO.getId(), atmDTO);

        String content = mockMvc.perform(patch("/atm/update/" + atmDTO.getId())
                .content(objectMapper.writeValueAsString(atmDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity()).andReturn().getResponse().getContentAsString();
        ErrorResponse result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals("DataAccessException", result.getCause());
        assertEquals("text", result.getMessage());
        verify(atmService).updateAtm(atmDTO.getId(), atmDTO);
    }
}