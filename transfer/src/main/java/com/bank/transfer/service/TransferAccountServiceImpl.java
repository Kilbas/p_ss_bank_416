package com.bank.transfer.service;

import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.repository.TransferAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferAccountServiceImpl implements TransferAccountService {
    private final TransferAccountRepository transferAccountRepository;

    @Override
    @Transactional
    public void addAccountTransfer(AccountTransfer accountTransfer) {
        transferAccountRepository.save(accountTransfer);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteAccountTransfer(long id) {
        AccountTransfer accountTransfer = transferAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AccountTransfer с id " + id + " не найден"));

        transferAccountRepository.delete(accountTransfer);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void updateAccountTransfer(AccountTransfer accountTransfer, long id) {
        AccountTransfer existingTransfer = transferAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AccountTransfer с id " + id + " не найден"));

        existingTransfer.setAmount(accountTransfer.getAmount());
        existingTransfer.setNumber(accountTransfer.getNumber());
        existingTransfer.setPurpose(accountTransfer.getPurpose());
        existingTransfer.setAccountDetailsId(accountTransfer.getAccountDetailsId());

        transferAccountRepository.save(existingTransfer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountTransfer> getAllAccountTransfers() {
        return transferAccountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = EntityNotFoundException.class)
    public AccountTransfer getAccountTransferById(long accountTransferId) {
        return transferAccountRepository.findById(accountTransferId).orElseThrow(() ->
                new EntityNotFoundException("Account transfer not found"));
    }
}
