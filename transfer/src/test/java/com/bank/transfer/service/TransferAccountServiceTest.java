package com.bank.transfer.service;

import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.repository.TransferAccountRepository;
import com.bank.transfer.serviceImpl.TransferAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
public class TransferAccountServiceTest {
    @Mock
    private TransferAccountRepository transferAccountRepository;

    @InjectMocks
    private TransferAccountServiceImpl transferAccountService;

    private AccountTransfer accountTransfer;

    @BeforeEach
    void setUp() {
        accountTransfer = new AccountTransfer(
                123456789L,
                BigDecimal.valueOf(1000.00),
                "Test Purpose",
                1L
        );
        accountTransfer.setId(1L);
    }

    @Nested
    @DisplayName("Тесты для метода addAccountTransfer")
    class AddAccountTransferTests {

        @Test
        @DisplayName("Успешное добавление AccountTransfer в базу")
        void shouldSaveAndReturnAccountTransfer() {
            when(transferAccountRepository.save(accountTransfer)).thenReturn(accountTransfer);

            AccountTransfer savedTransfer = transferAccountService.addAccountTransfer(accountTransfer);

            assertNotNull(savedTransfer);
            assertEquals(accountTransfer, savedTransfer);
            verify(transferAccountRepository, times(1)).save(accountTransfer);
        }
    }

    @Nested
    @DisplayName("Тесты для метода deleteAccountTransfer")
    class DeleteAccountTransferTests {

        @Test
        @DisplayName("Успешное удаление AccountTransfer")
        void shouldDeleteAccountTransfer_WhenExists() {
            when(transferAccountRepository.findById(accountTransfer.getId())).thenReturn(Optional.of(accountTransfer));

            transferAccountService.deleteAccountTransfer(accountTransfer.getId());

            verify(transferAccountRepository, times(1)).delete(accountTransfer);
        }

        @Test
        @DisplayName("Выбрасывает EntityNotFoundException, если AccountTransfer не найден")
        void shouldThrowEntityNotFoundException_WhenNotExists() {
            when(transferAccountRepository.findById(accountTransfer.getId())).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                    transferAccountService.deleteAccountTransfer(accountTransfer.getId())
            );

            assertEquals("Не найден AccountTransfer с id" + accountTransfer.getId(), exception.getMessage());
            verify(transferAccountRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("Тесты для метода updateAccountTransfer")
    class UpdateAccountTransferTests {

        @Test
        @DisplayName("Успешное обновление AccountTransfer")
        void shouldUpdateAndReturnUpdatedAccountTransfer() {
            AccountTransfer updatedTransfer = new AccountTransfer(
                    987654321L,
                    BigDecimal.valueOf(2000.00),
                    "Updated Purpose",
                    2L
            );

            when(transferAccountRepository.findById(accountTransfer.getId())).thenReturn(Optional.of(accountTransfer));
            when(transferAccountRepository.save(accountTransfer)).thenReturn(accountTransfer);

            AccountTransfer result = transferAccountService.updateAccountTransfer(updatedTransfer, accountTransfer.getId());

            assertNotNull(result);
            assertEquals(updatedTransfer.getNumber(), result.getNumber());
            assertEquals(updatedTransfer.getAmount(), result.getAmount());
            assertEquals(updatedTransfer.getPurpose(), result.getPurpose());
            assertEquals(updatedTransfer.getAccountDetailsId(), result.getAccountDetailsId());
            verify(transferAccountRepository, times(1)).save(accountTransfer);
        }

        @Test
        @DisplayName("Выбрасывает EntityNotFoundException, если AccountTransfer не найден")
        void shouldThrowEntityNotFoundException_WhenNotExists() {
            AccountTransfer updatedAccountTransfer = new AccountTransfer(
                    987654321L,
                    BigDecimal.valueOf(2000.00),
                    "Updated Purpose",
                    2L
            );

            when(transferAccountRepository.findById(accountTransfer.getId())).thenReturn(Optional.empty());

            Exception exception = assertThrows(EntityNotFoundException.class,
                    () -> transferAccountService.updateAccountTransfer(updatedAccountTransfer, accountTransfer.getId()));

            assertEquals("Не найден AccountTransfer с id" + accountTransfer.getId(), exception.getMessage());
            verify(transferAccountRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Тесты для методов получения AccountTransfer")
    class GetAccountTransferTests {

        @Test
        @DisplayName("Успешное получение всех AccountTransfer")
        void shouldReturnListOfAccountTransfers() {
            List<AccountTransfer> transfers = List.of(accountTransfer);
            when(transferAccountRepository.findAll()).thenReturn(transfers);

            List<AccountTransfer> result = transferAccountService.getAllAccountTransfers();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(accountTransfer.getId(), result.get(0).getId());
            verify(transferAccountRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Успешное получение одного AccountTransfer")
        void shouldReturnAccountTransfer_WhenExists() {
            when(transferAccountRepository.findById(accountTransfer.getId())).thenReturn(Optional.of(accountTransfer));

            AccountTransfer result = transferAccountService.getAccountTransferById(accountTransfer.getId());

            assertNotNull(result);
            assertEquals(accountTransfer, result);
            verify(transferAccountRepository, times(1)).findById(accountTransfer.getId());
        }

        @Test
        @DisplayName("Выбрасывает EntityNotFoundException, если AccountTransfer не найден")
        void shouldThrowEntityNotFoundException_WhenNotExists() {
            when(transferAccountRepository.findById(accountTransfer.getId())).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                    transferAccountService.getAccountTransferById(accountTransfer.getId())
            );

            assertEquals("Не найден AccountTransfer с id" + accountTransfer.getId(), exception.getMessage());
            verify(transferAccountRepository, times(1)).findById(accountTransfer.getId());
        }
    }
}
