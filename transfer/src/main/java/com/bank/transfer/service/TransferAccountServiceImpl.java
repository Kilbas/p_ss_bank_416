package com.bank.transfer.service;

import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.repository.TransferAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

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
    @Transactional
    public void deleteAccountTransfer(AccountTransfer accountTransfer) {
        transferAccountRepository.delete(accountTransfer);
    }

    @Override
    @Transactional
    public void updateAccountTransfer(AccountTransfer accountTransfer) {
        transferAccountRepository.save(accountTransfer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountTransfer> getAllAccountTransfers() {
        return transferAccountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public AccountTransfer getAccountTransferById(long accountTransferId) {
        return transferAccountRepository.findById(accountTransferId).orElseThrow(() ->
                new NotFoundException("Account transfer not found"));
    }
}
