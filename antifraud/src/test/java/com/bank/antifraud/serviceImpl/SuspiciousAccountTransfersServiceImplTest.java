package com.bank.antifraud.serviceImpl;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.entity.SuspiciousAccountTransfers;
import com.bank.antifraud.mapper.SuspiciousAccountTransfersMapper;
import com.bank.antifraud.repository.SuspiciousAccountTransfersRepository;
import com.bank.antifraud.util.TestDataSuspiciousAccountTransfers;
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
class SuspiciousAccountTransfersServiceImplTest {

    @Mock
    private SuspiciousAccountTransfersRepository repository;

    @Mock
    private SuspiciousAccountTransfersMapper mapper;

    @InjectMocks
    private SuspiciousAccountTransfersServiceImpl service;

    private final SuspiciousAccountTransfersDTO dto = TestDataSuspiciousAccountTransfers.createSuspiciousAccountTransfersDTO();
    private final SuspiciousAccountTransfers entity = TestDataSuspiciousAccountTransfers.createSuspiciousAccountTransfersEntity();

    @Nested
    @DisplayName("Create New Account Transfer")
    class CreateNewAccountTransfer {

        @Test
        @DisplayName("Should create and return DTO")
        void createNewAccountTransfer_ShouldReturnDTO() {
            when(mapper.toEntity(any(SuspiciousAccountTransfersDTO.class))).thenReturn(entity);
            when(repository.save(any(SuspiciousAccountTransfers.class))).thenReturn(entity);
            when(mapper.toDTO(any(SuspiciousAccountTransfers.class))).thenReturn(dto);

            SuspiciousAccountTransfersDTO result = service.createNewAccountTransfer(dto);

            assertNotNull(result);
            assertEquals(dto.getId(), result.getId());
            assertEquals(dto.getAccountTransferId(), result.getAccountTransferId());
            verify(repository, times(1)).save(entity);
        }
    }

    @Nested
    @DisplayName("Update Account Transfer")
    class UpdateAccountTransfer {

        @Test
        @DisplayName("Should update and return DTO")
        void updateAccountTransfer_ShouldReturnUpdatedDTO() {
            when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
            when(mapper.toDTO(any(SuspiciousAccountTransfers.class))).thenReturn(dto);

            SuspiciousAccountTransfersDTO updatedDto = dto;
            updatedDto.setBlockedReason("Updated Reason");

            doAnswer(invocation -> {
                SuspiciousAccountTransfersDTO transferDTO = invocation.getArgument(0);
                SuspiciousAccountTransfers existingEntity = invocation.getArgument(1);
                existingEntity.setBlockedReason(transferDTO.getBlockedReason());
                return null;
            }).when(mapper).updateFromDto(any(SuspiciousAccountTransfersDTO.class), any(SuspiciousAccountTransfers.class));

            SuspiciousAccountTransfersDTO result = service.updateAccountTransfer(1L, updatedDto);

            assertNotNull(result);
            assertEquals("Updated Reason", result.getBlockedReason());
            verify(repository, times(1)).save(entity);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException if not found")
        void updateAccountTransfer_ShouldThrowExceptionIfNotFound() {
            when(repository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () ->
                    service.updateAccountTransfer(1L, dto));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when cardTransferId is changed")
        void updateCardTransfer_ShouldThrowExceptionWhenCardTransferIdChanged() {
            // Arrange
            when(repository.findById(1L)).thenReturn(Optional.of(entity));

            // Устанавливаем несовпадающее значение cardTransferId
            dto.setAccountTransferId(200L);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    service.updateAccountTransfer(1L, dto));

            assertEquals("Поле accountTransferId не может быть изменено.", exception.getMessage());

            verify(repository, times(1)).findById(1L);
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("Delete Account Transfer")
    class DeleteAccountTransfer {

        @Test
        @DisplayName("Should delete successfully")
        void deleteAccountTransfer_ShouldDelete() {
            when(repository.existsById(anyLong())).thenReturn(true);

            service.deleteAccountTransfer(1L);

            verify(repository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException if not found")
        void deleteAccountTransfer_ShouldThrowExceptionIfNotFound() {
            when(repository.existsById(anyLong())).thenReturn(false);

            assertThrows(EntityNotFoundException.class, () ->
                    service.deleteAccountTransfer(1L));
        }
    }

    @Nested
    @DisplayName("Find Account Transfer")
    class FindAccountTransfer {

        @Test
        @DisplayName("Should return DTO when found")
        void findByIdAccountTransfer_ShouldReturnDTO() {
            when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
            when(mapper.toDTO(any(SuspiciousAccountTransfers.class))).thenReturn(dto);

            SuspiciousAccountTransfersDTO result = service.findByIdAccountTransfer(1L);

            assertNotNull(result);
            assertEquals(dto.getId(), result.getId());
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException if not found")
        void findByIdAccountTransfer_ShouldThrowExceptionIfNotFound() {
            when(repository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () ->
                    service.findByIdAccountTransfer(1L));
        }

        @Test
        @DisplayName("Should return all transfers")
        void findAllAccountTransfers_ShouldReturnList() {
            when(repository.findAll()).thenReturn(List.of(entity));
            when(mapper.toDTO(any(SuspiciousAccountTransfers.class))).thenReturn(dto);

            List<SuspiciousAccountTransfersDTO> result = service.findAllAccountTransfers();

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
            // Arrange
            when(repository.findBySuspiciousReasonContainingIgnoreCase(anyString()))
                    .thenReturn(List.of(entity));
            when(mapper.toDTO(entity)).thenReturn(dto);

            // Act
            List<SuspiciousAccountTransfersDTO> result = service.findTransfersByReason("Large amount transfer");

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Large amount transfer", result.get(0).getSuspiciousReason());
            verify(repository, times(1)).findBySuspiciousReasonContainingIgnoreCase("Large amount transfer");
        }

        @Test
        @DisplayName("Should find all blocked transfers")
        void findBlockedTransfers_ShouldReturnBlockedTransfers() {
            // Arrange
            when(repository.findByBlockedTrue()).thenReturn(List.of(entity));
            when(mapper.toDTO(entity)).thenReturn(dto);

            // Act
            List<SuspiciousAccountTransfersDTO> result = service.findBlockedTransfers();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertTrue(result.get(0).isBlocked());
            verify(repository, times(1)).findByBlockedTrue();
        }

        @Test
        @DisplayName("Should find all suspicious transfers")
        void findSuspiciousTransfers_ShouldReturnSuspiciousTransfers() {
            // Arrange
            when(repository.findBySuspiciousTrue()).thenReturn(List.of(entity));
            when(mapper.toDTO(entity)).thenReturn(dto);

            // Act
            List<SuspiciousAccountTransfersDTO> result = service.findSuspiciousTransfers();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertTrue(result.get(0).isSuspicious());
            verify(repository, times(1)).findBySuspiciousTrue();
        }
    }
}
