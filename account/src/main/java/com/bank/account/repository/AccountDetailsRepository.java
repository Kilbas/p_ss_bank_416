package com.bank.account.repository;

import com.bank.account.entity.AccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccountDetailsRepository extends JpaRepository<AccountDetails, Long> {

    Optional<AccountDetails> findById(Long id);
    Optional<AccountDetails> findByAccountNumber(Long accountNumber);
    Optional<AccountDetails> findByBankDetailsId(Long bankDetailsId);
}
