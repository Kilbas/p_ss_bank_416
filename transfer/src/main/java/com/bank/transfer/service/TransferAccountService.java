package com.bank.transfer.service;

import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.model.PhoneTransfer;

import java.util.List;

public interface TransferAccountService {
    void addAccountTransfer(AccountTransfer phoneTransfer);
    void deleteAccountTransfer(AccountTransfer phoneTransfer);
    void updateAccountTransfer(AccountTransfer phoneTransfer);
    List<AccountTransfer> getAllAccountTransfers();
    AccountTransfer getAccountTransferById(long accountTransferId);
}
