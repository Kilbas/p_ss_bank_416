package com.bank.publicinfo.service.bankDetails;

import com.bank.publicinfo.dto.BankDetailsDTO;

import java.util.List;

public interface BankDetailsService {

    List<BankDetailsDTO> getAllBankDetails();

    BankDetailsDTO getBankDetails(Long id);

    BankDetailsDTO addBankDetail(BankDetailsDTO bankDetails);

    BankDetailsDTO updateBankDetail(Long id, BankDetailsDTO bankDetails);

    BankDetailsDTO deleteBankDetail(Long id);
}
