package com.bank.account.service;

import com.bank.account.DTO.AccountDetailsDTO;
import com.bank.account.entity.AccountDetails;

import java.util.List;

public interface AccountDetailsService {

    AccountDetails saveAccountDetails(AccountDetails accountDetails);
    AccountDetails updateAccountDetails(Long id, AccountDetailsDTO accountDetailsDTO);
    AccountDetails deleteAccountDetails(Long id);
    AccountDetails getAccountDetailsById(Long id);
    AccountDetails getAccountDetailsByAccountNumber(Long accountNumber);
    AccountDetails getAccountDetailsByBankDetailsId(Long bankDetailsId);
    List<AccountDetails> getAllAccountDetails();
}
