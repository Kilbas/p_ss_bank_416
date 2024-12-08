package com.bank.account.service;

import com.bank.account.DTO.AccountDetailsDTO;
import com.bank.account.entity.AccountDetails;
import com.bank.account.mapper.AccountDetailsMapper;
import com.bank.account.repository.AccountDetailsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AccountDetailsServiceImpl implements AccountDetailsService {

    private final AccountDetailsRepository accountDetailsRepository;
    private final AccountDetailsMapper accountDetailsMapper;

    @Override
    @Transactional
    public AccountDetails saveAccountDetails(AccountDetails accountDetails) {
        log.info("Получили информацию об аккаунте для сохранения: {}", accountDetails);
        AccountDetails newAccountDetails = accountDetailsRepository.save(accountDetails);
        log.info("Информацию об аккаунте успешно сохранили: {}", newAccountDetails);
        return newAccountDetails;
    }

    @Override
    @Transactional
    public AccountDetails updateAccountDetails(Long id, AccountDetailsDTO accountDetailsDTO) {
        log.info("Получили id: {}, для обновления", id);
        accountDetailsDTO.setId(id);
        AccountDetails accountDetails = accountDetailsRepository.findById(id).orElseThrow(()->{
            log.warn("Информация об аккаунте не найдена");
            return new EntityNotFoundException(String.format("Информация об аккаунте с id: %s, не найдена", id));
        });
        accountDetailsMapper.toDtoUpdate(accountDetailsDTO, accountDetails);
        AccountDetails updateAccountDetails = accountDetailsRepository.save(accountDetails);
        log.info("Информацию об аккаунте успешно обнавлена: {}", updateAccountDetails);
        return updateAccountDetails;
    }

    @Override
    @Transactional
    public AccountDetails deleteAccountDetails(Long id) {
        log.info("Поучили запрос на удаление информации об аккаунте с ID: {}", id);
        AccountDetails accountDetails = accountDetailsRepository.findById(id).orElseThrow(()->{
            log.warn("Информация об аккаунте с id: {}, не найдена", id);
            return new EntityNotFoundException(String.format("Информация об аккаунте c id: %s не найдена", id));
        });
        accountDetailsRepository.deleteById(id);
        log.info("Информация об аккаунте с ID: {}, удалена", id);
        return accountDetails;
    }

    @Override
    public AccountDetails getAccountDetailsById(Long id) {
        log.info("Попытка найти информацию об аккаунте по ее ID: {}", id);
        AccountDetails accountDetails = accountDetailsRepository.findById(id).orElseThrow(()->{
            log.warn("Информация об аккаунте с id: {} не найдена", id);
            return new EntityNotFoundException(String.format("Информация об аккаунте с id: %s, не найдена", id));
        });
        log.info("Информация об аккаунте по ее ID: {}, найдена", id);
        return accountDetails;
    }

    @Override
    public AccountDetails getAccountDetailsByAccountNumber(Long accountNumber) {
        log.info("Попытка найти информацию об аккаунте по ее номеру: " + accountNumber);
        AccountDetails accountDetails =  accountDetailsRepository.findByAccountNumber(accountNumber).orElseThrow(()->{
            log.warn("Информация об аккаунте по ее номеру: {} не найдена", accountNumber);
            return new EntityNotFoundException(String.format("Информация об аккаунте по ее номеру: %s, не найдена", accountNumber));
        });
        log.info("Информация об аккаунте по ее номеру: {}, найдена", accountNumber);
        return accountDetails;
    }

    @Override
    public AccountDetails getAccountDetailsByBankDetailsId(Long bankDetailsId) {
        log.info("Попытка найти информацию об аккаунте по ID банковской детализации: " + bankDetailsId);
        AccountDetails accountDetails = accountDetailsRepository.findByBankDetailsId(bankDetailsId).orElseThrow(()->{
            log.warn("Информация об аккаунте по ID банковской детализации: {} не найдена", bankDetailsId);
            return new EntityNotFoundException(String.format("Информация об аккаунте по идентификатору банковской детализации: %s, не найдена", bankDetailsId));
        });
        log.info("Информация об аккаунте по ее ID банковской детализации: {}, найдена", bankDetailsId);
        return accountDetails;
    }

    @Override
    public List<AccountDetails> getAllAccountDetails() {
        log.info("Попытка выдать информацию по всем аккаунтам");
        List<AccountDetails> accountDetails = accountDetailsRepository.findAll();
        log.info("Информацию по всем аккаунтам найдена");
        return accountDetails;
    }
}
