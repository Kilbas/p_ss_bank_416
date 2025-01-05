package com.bank.history.services;

import com.bank.history.dto.HistoryDTO;
import com.bank.history.mappers.HistoryMapper;
import com.bank.history.models.History;
import com.bank.history.repositories.HistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HistoryServiceImplTest {

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private HistoryMapper historyMapper;

    @InjectMocks
    private HistoryServiceImpl historyService;

    private static final HistoryDTO historyDTO1 = new HistoryDTO(
            1L, 2L, 3L, 4L,
            5L, 6L, 7L);

    private static final HistoryDTO historyDTO2 = new HistoryDTO(
            8L, 9L, 10L, 11L,
            12L, 13L, 14L);

    private static final History history1 = new History(
            1L, 2L, 3L, 4L,
            5L, 6L, 7L);

    private static final History history2 = new History(
            8L, 9L, 10L, 11L,
            12L, 13L, 14L);

    private static final History updatedHistory = new History(8L, 1L,
            10L, 11L, 12L, 13L, 14L);

    private static final Pageable pageable = PageRequest.of(0, 2);

    private static final Long invalidLong = -1L;

    private static final Long updatedField = 1L;

    @Test
    @DisplayName("Тест метода findAll в HistoryServiceImpl с валидными данными на входе")
    void testFindAllValid() {
        List<HistoryDTO> historyDTOList = returnDTOList(historyDTO1, historyDTO2);
        Page<HistoryDTO> pageDTO = new PageImpl<>(historyDTOList);
        List<History> historyList = returnHistoryList(history1, history2);
        Page<History> page = new PageImpl<>(historyList);
        when(historyRepository.findAll(pageable)).thenReturn(page);
        when(historyMapper.pageToDTO(page)).thenReturn(pageDTO);
        Page<HistoryDTO> result = historyService.findAll(pageable);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(result, pageDTO),
                () -> verify(historyRepository).findAll(pageable),
                () -> verify(historyMapper).pageToDTO(page)
        );
    }

    @Test
    @DisplayName("Тест метода findAll в HistoryServiceImpl при отсутствии в БД таблицы history")
    void testFindAllInvalid() {
        when(historyRepository.findAll(pageable)).thenThrow(InvalidDataAccessResourceUsageException.class);
        assertThrows(InvalidDataAccessResourceUsageException.class, () -> historyService.findAll(pageable));
    }

    @Test
    @DisplayName("Тест метода findAll в HistoryServiceImpl при ошибке маппинга")
    void testFindAllInvalid2() {
        when(historyRepository.findAll(pageable)).thenReturn(Page.empty());
        when(historyMapper.pageToDTO(Page.empty())).thenReturn(Page.empty());
        Page<HistoryDTO> result = historyService.findAll(pageable);
        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty()),
                () -> verify(historyRepository).findAll(pageable),
                () -> verify(historyMapper).pageToDTO(Page.empty())
        );
    }

    @Test
    @DisplayName("Тест метода findById в HistoryServiceImpl с валидными данными")
    void testFindByIdValid() {
        when(historyRepository.findById(history1.getId())).thenReturn(Optional.of(history1));
        when(historyMapper.toDTO(history1)).thenReturn(historyDTO1);
        HistoryDTO result = historyService.findById(history1.getId());
        assertAll(
                () -> assertEquals(result, historyDTO1),
                () -> assertNotNull(result),
                () -> verify(historyMapper).toDTO(history1),
                () -> verify(historyRepository).findById(history1.getId())
        );
    }

    @Test
    @DisplayName("Тест метода findById в HistoryServiceImpl с отрицательным long")
    void testFindByIdNegative() {
        when(historyRepository.findById(invalidLong)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> historyService.findById(invalidLong));
    }

    @Test
    @DisplayName("Тест метода findById в HistoryServiceImpl с переданным null")
    void testFindByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> historyService.findById(null));
    }

    @Test
    @DisplayName("Тест метода update в HistoryServiceImpl с валидными данными")
    void testUpdateValid() {
        History oldHistory = new History(8L, 9L, 10L, 11L,
                12L, 13L, 14L);
        when(historyRepository.findById(oldHistory.getId())).thenReturn(Optional.of(oldHistory));
        HistoryDTO changes = new HistoryDTO();
        changes.setTransferAuditId(updatedField);
        doAnswer(invocation -> {
            HistoryDTO dto = invocation.getArgument(0); // Получаем DTO
            History target = invocation.getArgument(1); // Получаем объект для обновления
            if (dto.getTransferAuditId() != null) {
                target.setTransferAuditId(dto.getTransferAuditId());
            }
            return null;
        }).when(historyMapper).toEntityUpdate(changes, oldHistory);
        historyService.update(changes, oldHistory.getId());
        historyService.update(changes, oldHistory.getId());
        assertEquals(updatedHistory, oldHistory);
    }

    @Test
    @DisplayName("Тест метода update в HistoryServiceImpl с null")
    void testUpdateNull() {
        assertThrows(IllegalArgumentException.class, () -> historyService.update(null, null));
    }

    @Test
    @DisplayName("Тест метода update в HistoryServiceImpl с несуществующим id ")
    void testUpdateInvalid() {
        when(historyRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertAll(
                () -> assertThrows(EntityNotFoundException.class, () -> historyService.update(mock(HistoryDTO.class), invalidLong)),
                () -> verify(historyRepository).findById(invalidLong)
        );
    }

    @Test
    @DisplayName("Тест метода save в HistoryServiceImpl с валидными данными")
    void testSaveValid() {
        when(historyMapper.toEntitySave(historyDTO1)).thenReturn(history1);
        when(historyRepository.save(history1)).thenReturn(history1);
        History result = historyService.save(historyDTO1);
        assertAll(
                () -> assertEquals(result, history1),
                () -> assertNotNull(result),
                () -> verify(historyRepository).save(history1),
                () -> verify(historyMapper).toEntitySave(historyDTO1)
        );
    }

    @Test
    @DisplayName("Тест метода save в HistoryServiceImpl с переданным null")
    void testSaveNull() {
        assertThrows(IllegalArgumentException.class, () -> historyService.save(null));
    }

    @Test
    @DisplayName("Тест метода save в HistoryServiceImpl при отсутствии в БД таблицы history")
    void testSaveInvalid() {
        when(historyRepository.save(any())).thenThrow(InvalidDataAccessResourceUsageException.class);
        assertThrows(InvalidDataAccessResourceUsageException.class, () -> historyService.save(historyDTO1));
    }

    @Test
    @DisplayName("Тест метода save в HistoryServiceImpl при нарушении ограничений таблицы")
    void testSaveInvalid2() {
        when(historyRepository.save(any())).thenThrow(ConstraintViolationException.class);
        assertThrows(ConstraintViolationException.class, () -> historyService.save(historyDTO1));
    }

    @Test
    @DisplayName("Тест метода deleteById в HistoryServiceImpl с валидными данными")
    void testDeleteByIdValid() {
        when(historyRepository.findById(history1.getId())).thenReturn(Optional.of(history1));
        doAnswer(invocation -> null).when(historyRepository).deleteById(history1.getId());
        historyService.deleteById(history1.getId());
        verify(historyRepository).deleteById(history1.getId());
    }

    @Test
    @DisplayName("Тест метода deleteById в HistoryServiceImpl с несуществующим id")
    void testDeleteByIdInvalid() {
        when(historyRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertAll(
                () -> assertThrows(EntityNotFoundException.class, () -> historyService.deleteById(anyLong())),
                () -> verify(historyRepository).findById(anyLong())
        );

    }

    @Test
    @DisplayName("Тест метода deleteById в HistoryServiceImpl с переданным null")
    void testDeleteByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> historyService.deleteById(null));
    }

    List<HistoryDTO> returnDTOList(HistoryDTO... historyDTOs) {
        List<HistoryDTO> historyDTOList = new ArrayList<>();
        Collections.addAll(historyDTOList, historyDTOs);
        return historyDTOList;
    }

    List<History> returnHistoryList(History... histories) {
        List<History> historyList = new ArrayList<>();
        Collections.addAll(historyList, histories);
        return historyList;
    }
}
