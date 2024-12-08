package com.bank.transfer.service;

import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.repository.TransferAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TransferAccountServiceImpl implements TransferAccountService {
    private final TransferAccountRepository transferAccountRepository;

    @Override
    @Transactional
    public AccountTransfer addAccountTransfer(AccountTransfer accountTransfer) {
        transferAccountRepository.save(accountTransfer);
        log.info("Трансфер AccountTransfer успешно добавлен");
        return  accountTransfer;
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public AccountTransfer deleteAccountTransfer(long id) {
        AccountTransfer accountTransfer = transferAccountRepository.findById(id)
                .orElseThrow(() -> logAndThrowEntityNotFoundException(id));

        transferAccountRepository.delete(accountTransfer);
        log.info("Удален AccountTransfer c id {}", id);
        return accountTransfer;
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public AccountTransfer updateAccountTransfer(AccountTransfer accountTransfer, long id) {
        AccountTransfer existingTransfer = transferAccountRepository.findById(id)
                .orElseThrow(() -> logAndThrowEntityNotFoundException(id));

        existingTransfer.setAmount(accountTransfer.getAmount());
        existingTransfer.setNumber(accountTransfer.getNumber());
        existingTransfer.setPurpose(accountTransfer.getPurpose());
        existingTransfer.setAccountDetailsId(accountTransfer.getAccountDetailsId());

        transferAccountRepository.save(existingTransfer);
        log.info("Обновлены данные о трансфере AccountTransfer id {}", id);

        return existingTransfer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountTransfer> getAllAccountTransfers() {
        log.info("Получены все AccountTransfer");

        return transferAccountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = EntityNotFoundException.class)
    public AccountTransfer getAccountTransferById(long accountTransferId) {
        log.info("Поиск AccountTransfer c id {}", accountTransferId);

        return transferAccountRepository.findById(accountTransferId).
                orElseThrow(() -> logAndThrowEntityNotFoundException(accountTransferId));
    }

    private EntityNotFoundException logAndThrowEntityNotFoundException(long id) {
        log.error("Не найден AccountTransfer с указанным id {}", id);

        return new EntityNotFoundException("Не найден AccountTransfer с id" + id);
    }
}
