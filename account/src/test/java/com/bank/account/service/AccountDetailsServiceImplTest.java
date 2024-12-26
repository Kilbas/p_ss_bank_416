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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@DisplayName("Тесты для класса AccountDetailsServiceImpl")
@ExtendWith(MockitoExtension.class)
class AccountDetailsServiceImplTest {

    private AccountDetailsDTO accountDetailsDTO;
    private AccountDetailsDTO updateAccountDetailsDTO;
    private AccountDetails accountDetails;

    private final Long id = 1L;
    private final String illegalArgumentExceptionMessage = "Идентификатор не может быть null";
    private final String displayNameIllegalArgumentException = "Выбрасывает IllegalArgumentException, если id: null";
    private final String displayNameEntityNotFoundException = "Выбрасывает EntityNotFoundException, если id: не найден";

    private static final BigDecimal money = BigDecimal.valueOf(18000000.00);

    @Mock
    private AccountDetailsRepository accountDetailsRepository;

    @Spy
    private AccountDetailsMapper accountDetailsMapper = new AccountDetailsMapperImpl();

    @InjectMocks
    private AccountDetailsServiceImpl accountDetailsService;

    @BeforeEach
    void setUp() {
        accountDetailsDTO = createAccountDetailsDTO();
        updateAccountDetailsDTO = new AccountDetailsDTO();
        accountDetails = createAccountDetails();
    }

    private AccountDetailsDTO createAccountDetailsDTO() {
        return new AccountDetailsDTO(null, id, id, id, money, false, id);
    }

    private AccountDetails createAccountDetails() {
        return new AccountDetails(id, id, id, id, money, false, id);
    }

    private String stringFormatEntityNotFoundException (Long id) {
        return String.format("Информация об аккаунте с идентификатором: %s, не найдена", id);
    }

    private void assertIllegalArgumentException(Runnable action, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action::run);
        assertEquals(expectedMessage, exception.getMessage());
    }

    private void assertEntityNotFoundException(Runnable action, String expectedMessage) {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, action::run);
        assertEquals(expectedMessage, exception.getMessage());
    }

    private void verifyFindByIdCalledOnce(Long id) {
        verify(accountDetailsRepository, times(1)).findById(id);
    }

    private void verifySaveCalledOnce() {
        verify(accountDetailsRepository, times(1)).save(any(AccountDetails.class));
    }

    @Nested
    @DisplayName("Тесты для метода saveAccountDetails")
    class saveAccountDetailsTest {

        @DisplayName("Успешное сохранение AccountDetails")
        @Test
        void testSaveAccountDetailsPositive() {
            when(accountDetailsRepository.save(accountDetailsMapper.toEntitySave(accountDetailsDTO))).thenReturn(accountDetails);

            AccountDetailsDTO result = accountDetailsService.saveAccountDetails(accountDetailsDTO);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(id, result.getId()),
                    () -> verifySaveCalledOnce()
            );
        }

        @Test
        @DisplayName("Выбрасывает исключение, если accountDetailsDTO равен null")
        void testSaveAccountDetailsNullDTO() {
            String errorMessage = "Объект accountDetailsDTO не может быть null";

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountDetailsService.saveAccountDetails(null));

            assertAll(
                    () -> assertEquals(errorMessage, exception.getMessage()),
                    () -> verify(accountDetailsRepository, never()).save(any()),
                    () -> verify(accountDetailsMapper, never()).toEntitySave(any()),
                    () -> verify(accountDetailsMapper, never()).toDto(any())
            );
        }

        @Test
        @DisplayName("Выбрасывает исключение, если поля accountDetailsDTO равны null")
        void testSaveAccountDetailsNullFields() {
            String errorMessage = "Поля объекта accountDetailsDTO не могут быть null";

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountDetailsService.saveAccountDetails(updateAccountDetailsDTO));

            assertAll(
                    () -> assertEquals(errorMessage, exception.getMessage()),
                    () -> verify(accountDetailsRepository, never()).save(any()),
                    () -> verify(accountDetailsMapper, never()).toEntitySave(any()),
                    () -> verify(accountDetailsMapper, never()).toDto(any())
            );
        }
    }

    @Nested
    @DisplayName("Тесты для метода updateAccountDetails")
    class updateAccountDetailsTest {

        @DisplayName("Успешное обновление AccountDetails")
        @Test
        void testUpdateAccountDetailsPositive() {
            updateAccountDetailsDTO.setMoney(BigDecimal.valueOf(28000000.00));

            when(accountDetailsRepository.findById(id)).thenReturn(Optional.of(accountDetails));
            accountDetailsMapper.toDtoUpdate(updateAccountDetailsDTO, accountDetails);
            when(accountDetailsRepository.save(accountDetails)).thenReturn(accountDetails);

            AccountDetailsDTO result = accountDetailsService.updateAccountDetails(id, updateAccountDetailsDTO);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(updateAccountDetailsDTO.getMoney(), result.getMoney()),
                    () -> verifyFindByIdCalledOnce(id),
                    () -> verifySaveCalledOnce()
            );
        }

        @DisplayName(displayNameIllegalArgumentException)
        @Test
        void testUpdateAccountDetailsNegativeIdNull() {
            assertIllegalArgumentException(
                    () -> accountDetailsService.updateAccountDetails(null, null),
                    illegalArgumentExceptionMessage
            );
            verify(accountDetailsRepository, never()).save(any());
        }

        @DisplayName("Выбрасывает EntityNotFoundException, если id: не найден")
        @Test
        void testUpdateAccountDetailsNegativeEntityNotFoundException() {
            assertEntityNotFoundException(
                    () -> accountDetailsService.updateAccountDetails(id, updateAccountDetailsDTO),
                    stringFormatEntityNotFoundException(id)
            );
            verify(accountDetailsRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Тесты для метода deleteAccountDetails")
    class deleteAccountDetailsTest {

        @DisplayName("Успешное удаление AccountDetails")
        @Test
        void testDeleteAccountDetailsPositive() {
            when(accountDetailsRepository.findById(id)).thenReturn(Optional.of(accountDetails));
            doNothing().when(accountDetailsRepository).deleteById(id);

            accountDetailsService.deleteAccountDetails(id);

            verify(accountDetailsRepository, times(1)).deleteById(id);
        }

        @DisplayName(displayNameIllegalArgumentException)
        @Test
        void testDeleteAccountDetailsNegativeIllegalArgumentException() {
            assertIllegalArgumentException(
                    () -> accountDetailsService.deleteAccountDetails(null),
                    illegalArgumentExceptionMessage
            );
            verify(accountDetailsRepository, never()).deleteById(id);
        }

        @DisplayName(displayNameEntityNotFoundException)
        @Test
        void testDeleteAccountDetailsNegativeEntityNotFoundException() {
            assertEntityNotFoundException(
                    () -> accountDetailsService.deleteAccountDetails(id),
                    stringFormatEntityNotFoundException(id)
            );
            verify(accountDetailsRepository, never()).deleteById(id);
        }
    }

    @Nested
    @DisplayName("Тесты для метода getAccountDetailsById")
    class getAccountDetailsByIdTest {

        @DisplayName("Успешное возвращение AccountDetailsDTO")
        @Test
        void testGetAccountDetailsByIdPositive() {
            when(accountDetailsRepository.findById(id)).thenReturn(Optional.of(accountDetails));

            AccountDetailsDTO result = accountDetailsService.getAccountDetailsById(id);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(id, result.getId()),
                    () -> verifyFindByIdCalledOnce(id)
            );
        }

        @DisplayName(displayNameIllegalArgumentException)
        @Test
        void testGetAccountDetailsByIdNegativeIllegalArgumentException() {
            assertIllegalArgumentException(
                    () -> accountDetailsService.getAccountDetailsById(null),
                    illegalArgumentExceptionMessage
            );
            verify(accountDetailsRepository, never()).findById(id);
        }

        @DisplayName(displayNameEntityNotFoundException)
        @Test
        void testGetAccountDetailsByIdNegativeEntityNotFoundException() {
            assertEntityNotFoundException(
                    () -> accountDetailsService.getAccountDetailsById(id),
                    stringFormatEntityNotFoundException(id)
            );
            verify(accountDetailsRepository, times(1)).findById(id);
        }
    }

    @Nested
    @DisplayName("Тесты для метода GetAccountDetailsByAccountNumber")
    class getAccountDetailsByAccountNumberTest {

        @DisplayName("Успешное возвращение AccountDetailsDTO")
        @Test
        void testGetAccountDetailsByAccountNumberPositive() {
            when(accountDetailsRepository.findByAccountNumber(accountDetails.getAccountNumber())).thenReturn(Optional.of(accountDetails));

            AccountDetailsDTO result = accountDetailsService.getAccountDetailsByAccountNumber(accountDetails.getAccountNumber());

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(accountDetails.getAccountNumber(), result.getId()),
                    () -> verify(accountDetailsRepository, times(1)).findByAccountNumber(id)
            );
        }

        @DisplayName("Выбрасывает IllegalArgumentException, если номер счета: null")
        @Test
        void testGetAccountDetailsByAccountNumberNegativeIllegalArgumentException() {
            assertIllegalArgumentException(
                    () -> accountDetailsService.getAccountDetailsByAccountNumber(null),
                    "Номер счета не может быть null"
            );
            verify(accountDetailsRepository, never()).findByAccountNumber(id);
        }

        @DisplayName("Выбрасывает EntityNotFoundException, если номер счета: не найден")
        @Test
        void testGetAccountDetailsByAccountNumberNegativeEntityNotFoundException() {
            assertEntityNotFoundException(
                    () -> accountDetailsService.getAccountDetailsByAccountNumber(id),
                    String.format("Информация по номеру счёта: %s, не найдена", id)
            );
            verify(accountDetailsRepository, times(1)).findByAccountNumber(id);
        }
    }

    @Nested
    @DisplayName("Тесты для метода getAccountDetailsByBankDetailsId")
    class getAccountDetailsByBankDetailsIdTest {

        @DisplayName("Успешное возвращение AccountDetailsDTO")
        @Test
        void testGetAccountDetailsByBankDetailsIdPositive() {
            when(accountDetailsRepository.findByBankDetailsId(accountDetails.getBankDetailsId())).thenReturn(Optional.of(accountDetails));

            AccountDetailsDTO result = accountDetailsService.getAccountDetailsByBankDetailsId(accountDetails.getBankDetailsId());

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(accountDetails.getBankDetailsId(), result.getId()),
                    () -> verify(accountDetailsRepository, times(1)).findByBankDetailsId(accountDetails.getBankDetailsId())
            );
        }

        @DisplayName("Выбрасывает IllegalArgumentException, если технический идентификатор на реквизиты банка: null")
        @Test
        void testGetAccountDetailsByBankDetailsIdNegativeIllegalArgumentException() {
            assertIllegalArgumentException(
                    () -> accountDetailsService.getAccountDetailsByBankDetailsId(null),
                    "Технический идентификатор на реквизиты банка не может быть null"
            );
            verify(accountDetailsRepository, never()).findByBankDetailsId(id);
        }

        @DisplayName("Выбрасывает EntityNotFoundException, если технический идентификатор на реквизиты банка: не найден")
        @Test
        void testGetAccountDetailsByBankDetailsIdNegativeEntityNotFoundException() {
            assertEntityNotFoundException(
                    () -> accountDetailsService.getAccountDetailsByBankDetailsId(accountDetails.getBankDetailsId()),
                    String.format("Информация по техническому идентификатору на реквизиты банка: %s, не найдена", accountDetails.getBankDetailsId())
            );
            verify(accountDetailsRepository, times(1)).findByBankDetailsId(accountDetails.getBankDetailsId());
        }
    }

    @Nested
    @DisplayName("Тесты для метода getAllAccountDetails")
    class getAllAccountDetailsTest {

        @DisplayName("Успешное возвращение страницы AccountDetailsDTO")
        @Test
        void testGetAllAccountDetailsPositive() {
            Page<AccountDetails> page = new PageImpl<>(List.of(accountDetails));

            when(accountDetailsRepository.findAll(any(Pageable.class))).thenReturn(page);

            Page<AccountDetailsDTO> result = accountDetailsService.getAllAccountDetails(0, 10);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(1, result.getContent().size()),
                    () -> assertEquals(accountDetails.getId(), result.getContent().get(0).getId())
            );
        }
    }

    @Nested
    @DisplayName("Тесты для метода buildErrorMessage")
    class buildErrorMessageTest {

        @DisplayName("Успешное форматирование сообщения об ошибке")
        @Test
        void testBuildErrorMessagePositive() {
            String message = "Информация об аккаунте с идентификатором: %s, не найдена";

            String result = accountDetailsService.buildErrorMessage(message, id);

            assertEquals("Информация об аккаунте с идентификатором: 1, не найдена", result);
        }
    }

    @Nested
    @DisplayName("Тесты для метода validateArgs")
    class validateArgsTest {

        @DisplayName("Успешная проверка аргументов")
        @Test
        void testValidateArgsPositive() {
              assertDoesNotThrow(() -> accountDetailsService.validateArgs("Аргумент не может быть null", id, "test", BigDecimal.ONE));
        }

        @DisplayName("Выбрасывает IllegalArgumentException, если аргумент равен null")
        @Test
        void testValidateArgsNegative() {
            assertThrows(IllegalArgumentException.class, () -> accountDetailsService.validateArgs("Аргумент не может быть null", id, null));
        }
    }
}
