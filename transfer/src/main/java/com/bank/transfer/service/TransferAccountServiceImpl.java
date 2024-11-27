package com.bank.transfer.service;

import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.repository.TransferAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferAccountServiceImpl implements TransferAccountService {

    @Override
    public void addAccountTransfer(AccountTransfer phoneTransfer) {

    }

    @Override
    public void deleteAccountTransfer(AccountTransfer phoneTransfer) {

    }

    @Override
    public void updateAccountTransfer(AccountTransfer phoneTransfer) {

    }

    @Override
    public List<AccountTransfer> getAllAccountTransfers() {
        return List.of();
    }

    @Override
    public AccountTransfer getAccountTransferById(long accountTransferId) {
        return null;
    }
}
