package com.bank.transfer.serviceImpl;

import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.repository.TransferAccountRepository;
import com.bank.transfer.service.TransferAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(rollbackFor = EntityNotFoundException.class)
@RequiredArgsConstructor
@Slf4j
public class TransferAccountServiceImpl implements TransferAccountService {
    private final TransferAccountRepository transferAccountRepository;

    @Override
    public AccountTransfer addAccountTransfer(AccountTransfer accountTransfer) {
        transferAccountRepository.save(accountTransfer);

        return  accountTransfer;
    }

    @Override
    public void deleteAccountTransfer(long id) {
        AccountTransfer accountTransfer = transferAccountRepository.findById(id)
                .orElseThrow(() -> logAndThrowEntityNotFoundException(id));

        transferAccountRepository.delete(accountTransfer);
    }

    @Override
    public AccountTransfer updateAccountTransfer(AccountTransfer accountTransfer, long id) {
        AccountTransfer existingTransfer = transferAccountRepository.findById(id)
                .orElseThrow(() -> logAndThrowEntityNotFoundException(id));

        existingTransfer.setAmount(accountTransfer.getAmount());
        existingTransfer.setNumber(accountTransfer.getNumber());
        existingTransfer.setPurpose(accountTransfer.getPurpose());
        existingTransfer.setAccountDetailsId(accountTransfer.getAccountDetailsId());

        transferAccountRepository.save(existingTransfer);
        return existingTransfer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountTransfer> getAllAccountTransfers() {
        return transferAccountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public AccountTransfer getAccountTransferById(long accountTransferId) {
        return transferAccountRepository.findById(accountTransferId).
                orElseThrow(() -> logAndThrowEntityNotFoundException(accountTransferId));
    }

    private EntityNotFoundException logAndThrowEntityNotFoundException(long id) {
        log.error("Не найден AccountTransfer с указанным id {}", id);

        return new EntityNotFoundException("Не найден AccountTransfer с id" + id);
    }
}
