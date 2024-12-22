package com.bank.account.service;

import com.bank.account.DTO.AccountDetailsDTO;
import com.bank.account.entity.AccountDetails;
import com.bank.account.mapper.AccountDetailsMapper;
import com.bank.account.mapper.AccountDetailsMapperImpl;
import com.bank.account.repository.AccountDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class AccountDetailsServiceImplTest {

    private static final AccountDetailsDTO updateAccountDetailsDTO = new AccountDetailsDTO();
    private static final AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO(1L, 1L, 1L, 1L, money, false, 1L);
    private static final AccountDetails accountDetailsEntity = new AccountDetails(0L, 1L, 1L, 1L, money, false, 1L);
    private static final AccountDetails oldAccountDetailsEntity = new AccountDetails(1L, 1L, 1L, 1L, null, false, 1L);

    @Mock
    private AccountDetailsRepository accountDetailsRepository;

    @Spy
    private AccountDetailsMapper accountDetailsMapper = new AccountDetailsMapperImpl();

    @InjectMocks
    private AccountDetailsServiceImpl accountDetailsService;

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
    @DisplayName("Тесты для метода saveAccountDetails")
    class saveAccountDetailsTest {

        @Test
        @DisplayName("saveAccountDetails успешное сохранение AccountDetails")
        public void testSaveAccountDetailsPositive() {

            when(accountDetailsRepository.save(any(AccountDetails.class))).thenReturn(accountDetailsEntity);

            AccountDetailsDTO result = accountDetailsService.saveAccountDetails(accountDetailsDTO);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(accountDetailsDTO.getAccountNumber(), result.getAccountNumber()),
                    () -> assertEquals(accountDetailsDTO.getMoney(), result.getMoney()),
                    () -> verify(accountDetailsRepository, times(1)).save(any(AccountDetails.class))
            );
        }

        @DisplayName("saveAccountDetails выбрасывает IllegalArgumentException, при accountDetailsDTO: null")
        @Test
        public void testSaveAccountDetailsNegativeAccountDetailsDtoNull() {

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountDetailsService.saveAccountDetails(null));

            assertEquals("Объект accountDetailsDTO не может быть null", exception.getMessage());
        }

        @DisplayName("saveAccountDetails выбрасывает RuntimeException, если сохранение в репозитории завершается ошибкой")
        @Test
        public void testSaveAccountDetailsNegativeRepositorySaveError() {

            when(accountDetailsRepository.save(accountDetailsEntity)).thenThrow(new RuntimeException("Ошибка сохранения"));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> accountDetailsService.saveAccountDetails(accountDetailsDTO));

            assertAll(
                    () -> assertEquals("Ошибка сохранения", exception.getMessage()),
                    () -> verify(accountDetailsRepository, times(1)).save(accountDetailsEntity)
            );
        }
    }

    @Nested
    @DisplayName("Тесты для метода updateAccountDetails")
    class updateAccountDetailsTest {

        @DisplayName("updateAccountDetails успешное обновление AccountDetails")
        @Test
        public void testUpdateAccountDetailsPositive() {

            Long id = 1L;

            updateAccountDetailsDTO.setMoney(money);

            when(accountDetailsRepository.findById(id)).thenReturn(Optional.of(oldAccountDetailsEntity));

            when(accountDetailsRepository.save(any(AccountDetails.class))).thenReturn(accountDetailsEntity);

            AccountDetailsDTO result = accountDetailsService.updateAccountDetails(id, updateAccountDetailsDTO);

            assertAll(
                    () ->assertNotNull(result),
                    () ->assertEquals(updateAccountDetailsDTO.getMoney(), result.getMoney()),
                    () ->verify(accountDetailsRepository, times(1)).findById(id),
                    () ->verify(accountDetailsRepository, times(1)).save(any(AccountDetails.class))
            );
        }

        @DisplayName("updateAccountDetails выбрасывает IllegalArgumentException, при accountDetailsDTO: null")
        @Test
        public void testUpdateAccountDetailsNegativeAccountDetailsIdNull() {

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountDetailsService.updateAccountDetails(null,null));

            assertEquals("Идентификатор не может быть null", exception.getMessage());
        }

        @DisplayName("updateAccountDetails выбрасывает RuntimeException, если сохранение в репозитории завершается ошибкой")
        @Test
        public void testSaveAccountDetailsNegativeRepositorySaveError() {

            Long id = 1L;

            when(accountDetailsRepository.findById(id)).thenReturn(Optional.of(accountDetailsEntity));

            when(accountDetailsRepository.save(accountDetailsEntity)).thenThrow(new RuntimeException("Ошибка сохранения"));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> accountDetailsService.updateAccountDetails(id, accountDetailsDTO));

            assertAll(
                    () -> assertEquals("Ошибка сохранения", exception.getMessage()),
                    () -> verify(accountDetailsRepository, times(1)).save(accountDetailsEntity)
            );
        }
    }
}
