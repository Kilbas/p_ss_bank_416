package com.bank.transfer.service;

import com.bank.transfer.model.PhoneTransfer;
import com.bank.transfer.repository.TransferPhoneRepository;
import com.bank.transfer.serviceImpl.TransferPhoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransferPhoneServiceTest {
    @Mock
    private TransferPhoneRepository transferPhoneRepository;

    @InjectMocks
    private TransferPhoneServiceImpl transferPhoneService;

    private PhoneTransfer phoneTransfer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        phoneTransfer = new PhoneTransfer(
                123456789L,
                BigDecimal.valueOf(1000.00),
                "Test Purpose",
                1L
        );

        phoneTransfer.setId(1L);
    }

    @Test
    void addPhoneTransfer_ShouldSaveAndReturnPhoneTransfer() {
        when(transferPhoneRepository.save(phoneTransfer)).thenReturn(phoneTransfer);

        PhoneTransfer savedTransfer = transferPhoneService.addPhoneTransfer(phoneTransfer);

        assertNotNull(savedTransfer);
        assertEquals(phoneTransfer.getId(), savedTransfer.getId());
        verify(transferPhoneRepository, times(1)).save(phoneTransfer);
    }

    @Test
    void deletePhoneTransfer_ShouldDeleteAccountTransfer_WhenExists() {
        when(transferPhoneRepository.findById(phoneTransfer.getId())).thenReturn(Optional.of(phoneTransfer));

        transferPhoneService.deletePhoneTransfer(phoneTransfer.getId());

        verify(transferPhoneRepository, times(1)).delete(phoneTransfer);
    }

    @Test
    void deletePhoneTransfer_ShouldThrowEntityNotFoundException_WhenNotExists() {
        when(transferPhoneRepository.findById(phoneTransfer.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> transferPhoneService.deletePhoneTransfer(phoneTransfer.getId()));

        assertEquals("Не найден PhoneTransfer с id" + phoneTransfer.getId(), exception.getMessage());
        verify(transferPhoneRepository, never()).delete(any());
    }

    @Test
    void updatePhoneTransfer_ShouldUpdatePhoneTransfer_WhenExists() {
        PhoneTransfer updatedPhoneTransfer = new PhoneTransfer(
                987654321L,
                BigDecimal.valueOf(2000.00),
                "Updated Purpose",
                2L
        );

        when(transferPhoneRepository.findById(phoneTransfer.getId())).thenReturn(Optional.of(phoneTransfer));
        when(transferPhoneRepository.save(phoneTransfer)).thenReturn(updatedPhoneTransfer);

        PhoneTransfer result = transferPhoneService.updatePhoneTransfer(updatedPhoneTransfer, phoneTransfer.getId());

        assertNotNull(result);
        assertEquals(updatedPhoneTransfer.getPurpose(), result.getPurpose());
        assertEquals(updatedPhoneTransfer.getAmount(), result.getAmount());
        assertEquals(updatedPhoneTransfer.getAccountDetailsId(), result.getAccountDetailsId());
        assertEquals(updatedPhoneTransfer.getNumber(), result.getNumber());
        verify(transferPhoneRepository, times(1)).save(phoneTransfer);
    }

    @Test
    void updatePhoneTransfer_ShouldThrowEntityNotFoundException_WhenNotExists() {
        PhoneTransfer updatedPhoneTransfer = new PhoneTransfer(
                987654321L,
                BigDecimal.valueOf(2000.00),
                "Updated Purpose",
                2L
        );

        when(transferPhoneRepository.findById(phoneTransfer.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> transferPhoneService.updatePhoneTransfer(updatedPhoneTransfer, phoneTransfer.getId()));

        assertEquals("Не найден PhoneTransfer с id" + phoneTransfer.getId(), exception.getMessage());
        verify(transferPhoneRepository, never()).save(any());
    }

    @Test
    void getAllPhoneTransfers_ShouldReturnListOfPhoneTransfers() {
        when(transferPhoneRepository.findAll()).thenReturn(List.of(phoneTransfer));

        List<PhoneTransfer> result = transferPhoneService.getAllPhoneTransfers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(phoneTransfer.getId(), result.get(0).getId());
        verify(transferPhoneRepository, times(1)).findAll();
    }

    @Test
    void getPhoneTransferById_ShouldReturnPhoneTransfer_WhenExists() {
        when(transferPhoneRepository.findById(phoneTransfer.getId())).thenReturn(Optional.of(phoneTransfer));

        PhoneTransfer result = transferPhoneService.getPhoneTransferById(phoneTransfer.getId());

        assertNotNull(result);
        assertEquals(phoneTransfer.getId(), result.getId());
        assertEquals(phoneTransfer, result);
        verify(transferPhoneRepository, times(1)).findById(phoneTransfer.getId());
    }

    @Test
    void getPhoneTransferById_ShouldThrowEntityNotFoundException_WhenNotExists() {
        when(transferPhoneRepository.findById(phoneTransfer.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                transferPhoneService.getPhoneTransferById(phoneTransfer.getId())
        );

        assertEquals("Не найден PhoneTransfer с id" + phoneTransfer.getId(), exception.getMessage());
        verify(transferPhoneRepository, times(1)).findById(phoneTransfer.getId());
    }
}
