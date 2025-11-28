package com.bank.antifraud.serviceImpl;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;
import com.bank.antifraud.entity.SuspiciousPhoneTransfers;
import com.bank.antifraud.mapper.SuspiciousPhoneTransfersMapper;
import com.bank.antifraud.repository.SuspiciousPhoneTransfersRepository;
import com.bank.antifraud.util.TestDataSuspiciousPhoneTransfers;
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
class SuspiciousPhoneTransfersServiceImplTest {
    @Mock
    private SuspiciousPhoneTransfersRepository repository;
    @Mock
    private SuspiciousPhoneTransfersMapper mapper;

    @InjectMocks
    SuspiciousPhoneTransfersServiceImpl service;

    private final SuspiciousPhoneTransferDTO dto = TestDataSuspiciousPhoneTransfers.createSuspiciousPhoneTransfersDTO();
    private final SuspiciousPhoneTransfers entity = TestDataSuspiciousPhoneTransfers.createSuspiciousPhoneTransfersEntity();

    @Nested
    @DisplayName("Create new phone transfer")
    class CreateNewPhoneTransfer {

        @Test
        @DisplayName("Should create and return DTO")
        void CreateNewPhoneTransferShoutReturnDTO() {
            when(mapper.toEntity(any(SuspiciousPhoneTransferDTO.class))).thenReturn(entity);
            when(mapper.toDTO(any(SuspiciousPhoneTransfers.class))).thenReturn(dto);
            when(repository.save(any(SuspiciousPhoneTransfers.class))).thenReturn(entity);

            SuspiciousPhoneTransferDTO result = service.createNewPhoneTransfers(dto);

            assertNotNull(result);
            assertEquals(dto.getId(), result.getId());
            assertEquals(dto.getPhoneTransferId(), result.getPhoneTransferId());
            verify(repository, times(1)).save(entity);
        }
    }

    @Nested
    @DisplayName("Update phone transfer")
    class UpdatePhoneTransfer {

        @Test
        @DisplayName("Should update and return DTO")
        void updatePhoneTransfer_ShouldReturnUpdatedDTO() {
            when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
            when(mapper.toDTO(any(SuspiciousPhoneTransfers.class))).thenReturn(dto);

            dto.setBlockedReason("Updated Reason");

            doAnswer(invocation -> {
                SuspiciousPhoneTransferDTO transferDTO = invocation.getArgument(0);
                SuspiciousPhoneTransfers existing = invocation.getArgument(1);
                existing.setBlockedReason(transferDTO.getBlockedReason());
                return null;
            }).when(mapper).updateFromDto(any(SuspiciousPhoneTransferDTO.class), any(SuspiciousPhoneTransfers.class));

            SuspiciousPhoneTransferDTO result = service.updatePhoneTransfers(1L, dto);

            assertNotNull(result);
            assertEquals("Updated Reason", result.getBlockedReason());
            verify(repository, times(1)).save(entity);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException if not found")
        void updatePhoneTransfer_ShouldThrowExceptionIfNotFound() {
            when(repository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () ->
                    service.updatePhoneTransfers(1L, dto));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when phoneTransferId is changed")
        void updatePhoneTransfers_ShouldThrowExceptionWhenPhoneTransferIdChanged() {
            // Arrange
            when(repository.findById(1L)).thenReturn(Optional.of(entity));

            dto.setPhoneTransferId(200L);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    service.updatePhoneTransfers(1L, dto));

            assertEquals("Поле phoneTransferId не может быть изменено.", exception.getMessage());

            verify(repository, times(1)).findById(1L);
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("Should delete successfully")
    class DeletePhoneTransfer {

        @Test
        @DisplayName("Should delete successfully")
        void deletePhoneTransfer_ShouldDelete() {
            when(repository.existsById(anyLong())).thenReturn(true);

            service.deletePhoneTransfers(1L);

            verify(repository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException if not found")
        void deletePhoneTransfer_ShouldThrowExceptionIfNotFound() {
            when(repository.existsById(anyLong())).thenReturn(false);

            assertThrows(EntityNotFoundException.class, () ->
                    service.deletePhoneTransfers(1L));
        }
    }

    @Nested
    @DisplayName("Find phone transfer")
    class indCardTransfer {

        @Test
        @DisplayName("Should return DTO when found")
        void findByIdPhoneTransfer_ShouldReturnDTO() {
            when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
            when(mapper.toDTO(any(SuspiciousPhoneTransfers.class))).thenReturn(dto);

            SuspiciousPhoneTransferDTO result = service.findByIdPhoneTransfers(1L);

            assertNotNull(result);
            assertEquals(dto.getId(), result.getId());
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException if not found")
        void findByIdPhoneTransfer_ShouldThrowExceptionIfNotFound() {
            when(repository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () ->
                    service.findByIdPhoneTransfers(1L));
        }

        @Test
        @DisplayName("Should return all transfers")
        void findAllPhoneTransfers_ShouldReturnList() {
            when(repository.findAll()).thenReturn(List.of(entity));
            when(mapper.toDTO(any(SuspiciousPhoneTransfers.class))).thenReturn(dto);

            List<SuspiciousPhoneTransferDTO> result = service.findAllPhoneTransfers();

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
            when(repository.findBySuspiciousReasonContainingIgnoreCase(anyString())).thenReturn(List.of(entity));
            when(mapper.toDTO(entity)).thenReturn(dto);

            List<SuspiciousPhoneTransferDTO> result = service.findTransfersByReason("Large amount transfer");

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Large amount transfer", result.get(0).getSuspiciousReason());
            verify(repository, times(1)).findBySuspiciousReasonContainingIgnoreCase("Large amount transfer");

        }
    }

    @Test
    @DisplayName("Should find all blocked transfers")
    void findBlockedTransfers_ShouldReturnBlockedTransfers() {
        when(repository.findByBlockedTrue()).thenReturn(List.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        List<SuspiciousPhoneTransferDTO> result = service.findBlockedTransfers();

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

        List<SuspiciousPhoneTransferDTO> result = service.findSuspiciousTransfers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isSuspicious());
        verify(repository, times(1)).findBySuspiciousTrue();


    }


}