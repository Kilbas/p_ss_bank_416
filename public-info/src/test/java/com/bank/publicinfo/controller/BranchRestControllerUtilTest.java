package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.BranchDTO;
import com.bank.publicinfo.handler.ErrorResponse;
import com.bank.publicinfo.handler.GlobalExceptionHandler;
import com.bank.publicinfo.service.interfaceEntity.BranchService;
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
@DisplayName("Класс для тестирование BranchRestController.")
public class BranchRestControllerUtilTest {

    private static final Long ID = 11L;

    @Mock
    private BranchService branchService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(new BranchRestController(branchService))
                .setControllerAdvice(new Object[]{new GlobalExceptionHandler()}).addFilter((request, response, chain) -> {
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        }).setCustomArgumentResolvers(new HandlerMethodArgumentResolver[]{new PageableHandlerMethodArgumentResolver()}).build();
    }

    @Test
    @DisplayName("Получить список всех Branch.")
    void getAllBranch() throws Exception {
        Pageable pageable = PageRequest.of(1, 2);
        List<BranchDTO> all = List.of(CreateAtmAndBranch.createBranchDTO(), CreateAtmAndBranch.createBranchDTO());
        doReturn(all).when(branchService).getAllBranches(pageable);

        String content = mockMvc.perform(get("/branch").queryParam("page",
                String.valueOf(pageable.getPageNumber())).queryParam("size", String.valueOf(pageable.getPageSize()))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<BranchDTO> result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(all.size(), result.size());
        assertEquals(all.containsAll(result), result.containsAll(all));
        verify(branchService).getAllBranches(pageable);
    }

    @Test
    @DisplayName("Получение Branch. Успешный случай")
    void getBranchSuccess() throws Exception {
        BranchDTO dto = CreateAtmAndBranch.createBranchDTO();
        doReturn(dto).when(branchService).getBranch(dto.getId());

        ResultActions resultActions = mockMvc.perform(get("/branch/" + dto.getId())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        String content = resultActions.andReturn().getResponse().getContentAsString();
        BranchDTO result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertEquals(dto, result);
        verify(branchService).getBranch(dto.getId());
    }

    @Test
    @DisplayName("Получение Branch. Сущнось не найдена")
    void getBranchNotFoundEntity() throws Exception {
        doThrow(new EntityNotFoundException(String.format("Отделение не найден с id %s", ID))).when(branchService).getBranch(ID);

        ResultActions resultActions = mockMvc.perform(get("/branch/" + ID)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
        String content = resultActions.andReturn().getResponse().getContentAsString();
        ErrorResponse response = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals("EntityNotFound", response.getCause());
        assertEquals(String.format("Отделение не найден с id %s", ID), response.getMessage());
        verify(this.branchService).getBranch(ID);
    }

    @Test
    @DisplayName("Удаление Branch. Успешный случай.")
    void deleteBranchSuccess() throws Exception {
        doNothing().when(branchService).deleteBranch(ID);

        mockMvc.perform(delete("/branch/delete/" + ID)).andExpect(status().isNoContent());

        verify(branchService).deleteBranch(ID);
    }

    @Test
    @DisplayName("Создание Branch. Успешный случай.")
    void createBranchSuccess() throws Exception {
        BranchDTO branchDTO = CreateAtmAndBranch.createBranchDTO();
        doReturn(branchDTO).when(branchService).addBranch(branchDTO);

        String content = mockMvc.perform(post("/branch/create").content(objectMapper.writeValueAsString(branchDTO)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        BranchDTO result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals(branchDTO, result);
        verify(branchService).addBranch(branchDTO);
    }

    @Test
    @DisplayName("Создание Branch. NullPointerException.")
    void createBranchWhenNullPointerException() throws Exception {
        BranchDTO branchDTO = CreateAtmAndBranch.createBranchDTO();
        doThrow(new NullPointerException("text")).when(branchService).addBranch(branchDTO);

        String content = mockMvc.perform(post("/branch/create")
                .content(objectMapper.writeValueAsString(branchDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()).andReturn().getResponse().getContentAsString();
        ErrorResponse result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals("Exception", result.getCause());
        assertEquals("text", result.getMessage());
        verify(branchService).addBranch(branchDTO);
    }

    @Test
    @DisplayName("Создание Branch. Ошибка валидации тела запроса.")
    void createBranchNotValidRequest() throws Exception {
        BranchDTO branchDTO = CreateAtmAndBranch.createBranchDTO();
        branchDTO.setAddress(null);

        String content = mockMvc.perform(post("/branch/create")
                .content(objectMapper.writeValueAsString(branchDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
        List<ErrorResponse> result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(1, result.size());
        assertEquals("Вы не указали address", result.get(0).getMessage());
        assertEquals("address", result.get(0).getCause());
    }

    @Test
    @DisplayName("Изменение Branch. Успешный случай.")
    void updateBranchSuccess() throws Exception {
        BranchDTO branchDTO = CreateAtmAndBranch.createBranchDTO();
        doReturn(branchDTO).when(branchService).updateBranch(branchDTO.getId(), branchDTO);

        String content = mockMvc.perform(patch("/branch/update/" + branchDTO.getId()).content(objectMapper.writeValueAsString(branchDTO)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        BranchDTO result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals(branchDTO, result);
        verify(branchService).updateBranch(branchDTO.getId(), branchDTO);
    }

    @Test
    @DisplayName("Изменение Branch. DataAccessException.")
    void updateBranchWhenDataAccessException() throws Exception {
        BranchDTO branchDTO = CreateAtmAndBranch.createBranchDTO();
        doThrow(new DuplicateKeyException("text")).when(branchService).updateBranch(branchDTO.getId(), branchDTO);

        String content = mockMvc.perform(patch("/branch/update/" + branchDTO.getId())
                .content(objectMapper.writeValueAsString(branchDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity()).andReturn().getResponse().getContentAsString();
        ErrorResponse result = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals("DataAccessException", result.getCause());
        assertEquals("text", result.getMessage());
        verify(branchService).updateBranch(branchDTO.getId(), branchDTO);
    }
}