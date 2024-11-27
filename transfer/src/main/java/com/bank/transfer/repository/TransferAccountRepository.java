package com.bank.transfer.repository;

import com.bank.transfer.model.AccountTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferAccountRepository extends JpaRepository<AccountTransfer, Long> {
}
