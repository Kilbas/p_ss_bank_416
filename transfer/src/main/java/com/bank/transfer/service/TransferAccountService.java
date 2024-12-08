package com.bank.transfer.service;

import com.bank.transfer.model.AccountTransfer;

import java.util.List;

public interface TransferAccountService {
    AccountTransfer addAccountTransfer(AccountTransfer accountTransfer);
    void deleteAccountTransfer(long id);
    AccountTransfer updateAccountTransfer(AccountTransfer accountTransfer, long id);
    List<AccountTransfer> getAllAccountTransfers();
    AccountTransfer getAccountTransferById(long accountTransferId);
}
