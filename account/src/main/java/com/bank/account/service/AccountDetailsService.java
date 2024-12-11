package com.bank.account.service;

import com.bank.account.DTO.AccountDetailsDTO;
import org.springframework.data.domain.Page;

public interface AccountDetailsService {

    AccountDetailsDTO saveAccountDetails(AccountDetailsDTO accountDetailsDTO);
    AccountDetailsDTO updateAccountDetails(Long id, AccountDetailsDTO accountDetailsDTO);
    void deleteAccountDetails(Long id);

    AccountDetailsDTO getAccountDetailsById(Long id);
    AccountDetailsDTO getAccountDetailsByAccountNumber(Long accountNumber);
    AccountDetailsDTO getAccountDetailsByBankDetailsId(Long bankDetailsId);

    Page<AccountDetailsDTO> getAllAccountDetails(int page, int size);
}
