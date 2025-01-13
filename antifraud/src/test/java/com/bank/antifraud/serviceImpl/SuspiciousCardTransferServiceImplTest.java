package com.bank.antifraud.serviceImpl;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.entity.SuspiciousCardTransfer;
import com.bank.antifraud.mapper.SuspiciousCardTransferMapper;
import com.bank.antifraud.repository.SuspiciousCardTransferRepository;
import com.bank.antifraud.util.TestDataSuspiciousCardTransfers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SuspiciousCardTransferServiceImplTest {

    @Mock
    private SuspiciousCardTransferRepository repository;

    @Mock
    private SuspiciousCardTransferMapper mapper;

    @InjectMocks
    SuspiciousCardTransferServiceImpl service;

    final private SuspiciousCardTransferDTO dto = TestDataSuspiciousCardTransfers.createSuspiciousCardTransfersDTO();
    final private SuspiciousCardTransfer entity = TestDataSuspiciousCardTransfers.createSuspiciousCardTransfersEntity();


    @Nested
    @DisplayName("Create new cart transfer")
    class CreateNewCartTransfer {

        @Test
        @DisplayName("Should create and return DTO")
        void CreateNewCartTransferShoutReturnDTO() {
            when(mapper.toEntity(any(SuspiciousCardTransferDTO.class))).thenReturn(entity);
            when(repository.save(any(SuspiciousCardTransfer.class))).thenReturn(entity);
            when(mapper.toDTO(any(SuspiciousCardTransfer.class))).thenReturn(dto);

            SuspiciousCardTransferDTO result = service.createNewCardTransfer(dto);

            assertNotNull(result);
            assertEquals(dto.getId(), result.getId());
            assertEquals(dto.getCardTransferId(), result.getCardTransferId());
            verify(repository, times(1)).save(entity);

        }
    }

    @Nested
    @DisplayName("Update Account Transfer")
    class UpdateCardTransfer {

        @Test
        @DisplayName("Should update and return DTO")
        void updateCardTransfer_ShouldReturnUpdatedDTO() {
            when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
            when(mapper.toDTO(any(SuspiciousCardTransfer.class))).thenReturn(dto);

            SuspiciousCardTransferDTO updateDTO = dto;
            updateDTO.setBlockedReason("Updated Reason");

            doAnswer(invocation -> {
                SuspiciousCardTransferDTO transferDTO = invocation.getArgument(0);
                SuspiciousCardTransfer existing = invocation.getArgument(1);
                existing.setBlockedReason(transferDTO.getBlockedReason());
                return null;
            }).when(mapper).updateFromDto(any(SuspiciousCardTransferDTO.class), any(SuspiciousCardTransfer.class));

            SuspiciousCardTransferDTO result = service.updateCardTransfer(1L, updateDTO);

            assertNotNull(result);
            assertEquals("Updated Reason", result.getBlockedReason());
            verify(repository, times(1)).save(entity);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException if not found")
        void updateCardTransfer_ShouldThrowExceptionIfNotFound() {
            when(repository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () ->
                    service.updateCardTransfer(1L, dto));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when cardTransferId is changed")
        void updateCardTransfer_ShouldThrowExceptionWhenCardTransferIdChanged() {
            when(repository.findById(1L)).thenReturn(Optional.of(entity));

            dto.setCardTransferId(200L);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    service.updateCardTransfer(1L, dto));

            assertEquals("Поле cardTransferId не может быть изменено.", exception.getMessage());

            verify(repository, times(1)).findById(1L);
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("Delete Account Transfer")
    class DeleteCartTransfer {


        @Test
        @DisplayName("Should delete successfully")
        void deleteCardTransfer_ShouldDelete() {
            when(repository.existsById(anyLong())).thenReturn(true);

            service.deleteCardTransfer(1L);

            verify(repository, times(1)).deleteById(1L);

        }

        @Test
        @DisplayName("Should throw EntityNotFoundException if not found")
        void deleteCardTransfer_ShouldThrowExceptionIfNotFound() {
            when(repository.existsById(anyLong())).thenReturn(false);

            assertThrows(EntityNotFoundException.class, () ->
                    service.deleteCardTransfer(1L));
        }

    }

    @Nested
    @DisplayName("Find card transfer")
    class indCardTransfer {

        @Test
        @DisplayName("Should return DTO when found")
        void findByIdAccountTransfer_ShouldReturnDTO() {
            when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
            when(mapper.toDTO(any(SuspiciousCardTransfer.class))).thenReturn(dto);

            SuspiciousCardTransferDTO result = service.findByIdCardTransfer(1L);

            assertNotNull(result);
            assertEquals(dto.getId(), result.getId());
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException if not found")
        void findByIdCardTransfer_ShouldThrowExceptionIfNotFound() {
            when(repository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () ->
                    service.findByIdCardTransfer(1L));
        }

        @Test
        @DisplayName("Should return all transfers")
        void findAllCardTransfers_ShouldReturnList() {
            when(repository.findAll()).thenReturn(List.of(entity));
            when(mapper.toDTO(any(SuspiciousCardTransfer.class))).thenReturn(dto);

            List<SuspiciousCardTransferDTO> result = service.findAllCardTransfers();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("find transfers")
    class FindAnyTransfers {

        @Test
        @DisplayName("Should find transfers by reason")
        void findTransfersByReason_ShouldReturnMatchingTransfers() {
            when(repository.findBySuspiciousReasonContainingIgnoreCase(anyString()))
                    .thenReturn(List.of(entity));
            when(mapper.toDTO(entity)).thenReturn(dto);

            List<SuspiciousCardTransferDTO> result = service.findTransfersByReason("Large amount transfer");

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Large amount transfer", result.get(0).getSuspiciousReason());
            verify(repository, times(1)).findBySuspiciousReasonContainingIgnoreCase("Large amount transfer");

        }

        @Test
        @DisplayName("Should find all blocked transfers")
        void findBlockedTransfers_ShouldReturnBlockedTransfers() {
            when(repository.findByBlockedTrue()).thenReturn(List.of(entity));
            when(mapper.toDTO(entity)).thenReturn(dto);

            List<SuspiciousCardTransferDTO> result = service.findBlockedTransfers();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertTrue(result.get(0).isBlocked());
            verify(repository, times(1)).findByBlockedTrue();
        }

        @Test
        @DisplayName("Should find all suspicious transfers")
        void findSuspiciousTransfers_ShouldReturnSuspiciousTransfers() {
            when(repository.findBySuspiciousTrue()).thenReturn(List.of(entity));
            when(mapper.toDTO(entity)).thenReturn(dto);

            List<SuspiciousCardTransferDTO> result = service.findSuspiciousTransfers();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertTrue(result.get(0).isSuspicious());
            verify(repository, times(1)).findBySuspiciousTrue();


        }
    }
}