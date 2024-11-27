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
    @Transactional
    public void deleteAccountTransfer(long id) {
        transferAccountRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateAccountTransfer(AccountTransfer accountTransfer, long id) {
        AccountTransfer existingTransfer = transferAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AccountTransfer с id " + accountTransfer.getId() + " не найден"));

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
    @Transactional(readOnly = true)
    public AccountTransfer getAccountTransferById(long accountTransferId) {
        return transferAccountRepository.findById(accountTransferId).orElseThrow(() ->
                new EntityNotFoundException("Account transfer not found"));
    }
}
