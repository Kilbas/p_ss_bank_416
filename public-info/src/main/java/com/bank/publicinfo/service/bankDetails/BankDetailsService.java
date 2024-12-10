package com.bank.publicinfo.service.bankDetails;

import com.bank.publicinfo.dto.BankDetailsDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BankDetailsService {

    List<BankDetailsDTO> getAllBankDetails( Pageable pageable);

    BankDetailsDTO getBankDetails(Long id);

    BankDetailsDTO addBankDetail(BankDetailsDTO bankDetails);

    BankDetailsDTO updateBankDetail(Long id, BankDetailsDTO bankDetails);

    void deleteBankDetail(Long id);
}