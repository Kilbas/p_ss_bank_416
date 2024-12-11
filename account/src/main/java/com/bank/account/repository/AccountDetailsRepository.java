package com.bank.account.repository;

import com.bank.account.entity.AccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDetailsRepository extends JpaRepository<AccountDetails, Long> {

    @NonNull
    Optional<AccountDetails> findByAccountNumber(@NonNull Long accountNumber);

    @NonNull
    Optional<AccountDetails> findByBankDetailsId(@NonNull Long bankDetailsId);
}
