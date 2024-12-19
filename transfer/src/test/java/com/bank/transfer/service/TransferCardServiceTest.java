package com.bank.transfer.service;

import com.bank.transfer.model.CardTransfer;
import com.bank.transfer.repository.TransferCardRepository;
import com.bank.transfer.serviceImpl.TransferCardServiceImpl;
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

public class TransferCardServiceTest {

    @Mock
    private TransferCardRepository transferCardRepository;

    @InjectMocks
    private TransferCardServiceImpl transferCardService;

    private CardTransfer cardTransfer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cardTransfer = new CardTransfer(
                123456789L,
                BigDecimal.valueOf(1000.00),
                "Test Purpose",
                1L
        );

        cardTransfer.setId(1L);
    }

    @Test
    void addCardTransfer_ShouldSaveAndReturnCardTransfer() {
        when(transferCardRepository.save(cardTransfer)).thenReturn(cardTransfer);

        CardTransfer savedTransfer = transferCardService.addCardTransfer(cardTransfer);

        assertNotNull(savedTransfer);
        assertEquals(cardTransfer.getId(), savedTransfer.getId());
        assertEquals(cardTransfer, savedTransfer);
        verify(transferCardRepository, times(1)).save(cardTransfer);
    }

    @Test
    void deleteCardTransfer_ShouldDeleteAccountTransfer_WhenExists() {
        when(transferCardRepository.findById(cardTransfer.getId())).thenReturn(Optional.of(cardTransfer));

        transferCardService.deleteCardTransfer(cardTransfer.getId());

        verify(transferCardRepository, times(1)).delete(cardTransfer);
    }

    @Test
    void deleteCardTransfer_ShouldThrowEntityNotFoundException_WhenNotExists() {
        when(transferCardRepository.findById(cardTransfer.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> transferCardService.deleteCardTransfer(cardTransfer.getId()));

        assertEquals("Не найден CardTransfer с id" + cardTransfer.getId(), exception.getMessage());
        verify(transferCardRepository, never()).delete(any());
    }

    @Test
    void updateCardTransfer_ShouldUpdateCardTransfer_WhenExists() {
        CardTransfer updatedCardTransfer = new CardTransfer(
                987654321L,
                BigDecimal.valueOf(2000.00),
                "Updated Purpose",
                2L
        );

        when(transferCardRepository.findById(cardTransfer.getId())).thenReturn(Optional.of(cardTransfer));
        when(transferCardRepository.save(cardTransfer)).thenReturn(updatedCardTransfer);

        CardTransfer result = transferCardService.updateCardTransfer(updatedCardTransfer, cardTransfer.getId());

        assertNotNull(result);
        assertEquals(updatedCardTransfer.getPurpose(), result.getPurpose());
        assertEquals(updatedCardTransfer.getAmount(), result.getAmount());
        assertEquals(updatedCardTransfer.getAccountDetailsId(), result.getAccountDetailsId());
        assertEquals(updatedCardTransfer.getNumber(), result.getNumber());
        verify(transferCardRepository, times(1)).save(cardTransfer);
    }

    @Test
    void updateCardTransfer_ShouldThrowEntityNotFoundException_WhenNotExists() {
        CardTransfer updatedCardTransfer = new CardTransfer(
                987654321L,
                BigDecimal.valueOf(2000.00),
                "Updated Purpose",
                2L
        );

        when(transferCardRepository.findById(cardTransfer.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> transferCardService.updateCardTransfer(updatedCardTransfer, cardTransfer.getId()));

        assertEquals("Не найден CardTransfer с id" + cardTransfer.getId(), exception.getMessage());
        verify(transferCardRepository, never()).save(any());
    }

    @Test
    void getAllCardTransfers_ShouldReturnListOfCardTransfers() {
        when(transferCardRepository.findAll()).thenReturn(List.of(cardTransfer));

        List<CardTransfer> result = transferCardService.getAllCardTransfers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(cardTransfer.getId(), result.get(0).getId());
        verify(transferCardRepository, times(1)).findAll();
    }

    @Test
    void getCardTransferById_ShouldReturnCardTransfer_WhenExists() {
        when(transferCardRepository.findById(cardTransfer.getId())).thenReturn(Optional.of(cardTransfer));

        CardTransfer result = transferCardService.getCardTransferById(cardTransfer.getId());

        assertNotNull(result);
        assertEquals(cardTransfer.getId(), result.getId());
        assertEquals(cardTransfer, result);
        verify(transferCardRepository, times(1)).findById(cardTransfer.getId());
    }

    @Test
    void getCardTransferById_ShouldThrowEntityNotFoundException_WhenNotExists() {
        when(transferCardRepository.findById(cardTransfer.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                transferCardService.getCardTransferById(cardTransfer.getId())
        );

        assertEquals("Не найден CardTransfer с id" + cardTransfer.getId(), exception.getMessage());
        verify(transferCardRepository, times(1)).findById(cardTransfer.getId());
    }
}
