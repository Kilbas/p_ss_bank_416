package com.bank.account.service;

import com.bank.account.DTO.AccountDetailsDTO;
import com.bank.account.entity.AccountDetails;
import com.bank.account.mapper.AccountDetailsMapper;
import com.bank.account.repository.AccountDetailsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;

/**
 * Реализация сервиса для работы с деталями аккаунтов в таблице {@code account_details} схемы {@code account}.
 * Этот сервис предоставляет методы для сохранения, обновления, удаления и поиска деталей аккаунтов.
 * Также поддерживает пагинацию при получении всех записей.
 *
 * @author Александр Федотов
 * @version 1.0.0
 */

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AccountDetailsServiceImpl implements AccountDetailsService {

    private final AccountDetailsRepository accountDetailsRepository;
    private final AccountDetailsMapper accountDetailsMapper;

    /**
     * Сохраняет новые детали банковского счёта.
     *
     * @param accountDetailsDTO Объект DTO с деталями аккаунта, который нужно сохранить.
     * @return Объект DTO с сохраненными деталями аккаунта.
     */
    @Override
    @Transactional
    public AccountDetailsDTO saveAccountDetails(AccountDetailsDTO accountDetailsDTO) {
        return accountDetailsMapper
                .toDto(accountDetailsRepository
                        .save(accountDetailsMapper.toEntitySave(accountDetailsDTO)));
    }

    /**
     * Обновляет детали аккаунта по указанному идентификатору.
     *
     * @param id Технический идентификатор деталей аккаунта, которые нужно обновить. Не может быть null.
     * @param accountDetailsDTO Объект DTO с новыми данными для обновления.
     * @return Объект DTO с обновленными деталями аккаунта.
     * @throws IllegalArgumentException Если идентификатор равен null.
     * @throws EntityNotFoundException Если детали аккаунта с указанным идентификатором не найдены.
     */
    @Override
    @Transactional
    public AccountDetailsDTO updateAccountDetails(Long id, AccountDetailsDTO accountDetailsDTO) {
        validateArg(id, "Идентификатор не может быть null");

        AccountDetails accountDetails = accountDetailsMapper
                .toEntity(this.getAccountDetailsById(id));
        accountDetailsMapper.toDtoUpdate(accountDetailsDTO, accountDetails);
        return accountDetailsMapper
                .toDto(accountDetailsRepository.save(accountDetails));
    }

    /**
     * Удаляет детали аккаунта по указанному идентификатору.
     *
     * @param id Идентификатор деталей аккаунта, которые нужно удалить. Не может быть null.
     * @throws IllegalArgumentException Если идентификатор равен null.
     * @throws EntityNotFoundException Если детали аккаунта с указанным идентификатором не найдены.
     */
    @Override
    @Transactional
    public void deleteAccountDetails(Long id) {
        validateArg(id, "Идентификатор не может быть null");

        this.getAccountDetailsById(id);
        accountDetailsRepository.deleteById(id);
    }

    /**
     * Получает детали аккаунта по указанному идентификатору.
     *
     * @param id Идентификатор деталей аккаунта. Не может быть null.
     * @return Объект DTO с деталями аккаунта.
     * @throws IllegalArgumentException Если идентификатор равен null.
     * @throws EntityNotFoundException Если детали аккаунта с указанным идентификатором не найдены.
     */
    @Override
    public AccountDetailsDTO getAccountDetailsById(Long id) {
        validateArg(id, "Идентификатор не может быть null");

        return accountDetailsMapper
                .toDto(accountDetailsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        buildErrorMessage("Информация об аккаунте с идентификатором: %s, не найдена", id)
                )));
    }

    /**
     * Получает детали аккаунта по указанному номеру счета.
     *
     * @param accountNumber Номер счета. Не может быть null.
     * @return Объект DTO с деталями аккаунта.
     * @throws IllegalArgumentException Если номер счета равен null.
     * @throws EntityNotFoundException Если детали аккаунта с указанным номером счета не найдены.
     */
    @Override
    public AccountDetailsDTO getAccountDetailsByAccountNumber(Long accountNumber) {
        validateArg(accountNumber, "Номер счета не может быть null");

        return accountDetailsMapper
                .toDto(accountDetailsRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException(
                        buildErrorMessage("Информация по номеру счёта не найдена %s", accountNumber)
                )));
    }

    /**
     * Получает детали аккаунта по указанному техническому идентификатору на реквизиты банка.
     *
     * @param bankDetailsId Технический идентификатор на реквизиты банка. Не может быть null.
     * @return Объект DTO с деталями аккаунта.
     * @throws IllegalArgumentException Если технический идентификатор равен null.
     * @throws EntityNotFoundException Если детали аккаунта с указанным идентификатором не найдены.
     */
    @Override
    public AccountDetailsDTO getAccountDetailsByBankDetailsId(Long bankDetailsId) {
        validateArg(bankDetailsId, "Технический идентификатор на реквизиты банка не может быть null");

        return accountDetailsMapper
                .toDto(accountDetailsRepository.findByBankDetailsId(bankDetailsId)
                .orElseThrow(() -> new EntityNotFoundException(
                        buildErrorMessage("Информация по техническому идентификатору" +
                                " на реквизиты банка не найдена %s", bankDetailsId)
                )));
    }

    /**
     * Получает все детали аккаунтов с пагинацией.
     *
     * @param page Номер страницы (начиная с 0).
     * @param size Размер страницы (количество элементов на странице).
     * @return Страница с объектами DTO деталей аккаунтов.
     */
    @Override
    public Page<AccountDetailsDTO> getAllAccountDetails(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return accountDetailsRepository
                .findAll(pageable)
                .map(accountDetailsMapper::toDto);
    }

    /**
     * Формирует сообщение об ошибке с использованием указанного формата и значения.
     *
     * @param message Формат сообщения.
     * @param value Значение, которое нужно подставить в сообщение.
     * @return Сформированное сообщение об ошибке.
     */
    private String buildErrorMessage(String message, Object value) {
        return String.format(message, value);
    }

    /**
     * Проверяет, что указанный аргумент не равен null.
     *
     * @param arg Аргумент, который нужно проверить.
     * @param message Сообщение об ошибке, если аргумент равен null.
     * @throws IllegalArgumentException Если аргумент равен null.
     */
    private void validateArg(Long arg, String message) {
        Assert.notNull(arg, message);
    }
}
