package com.bank.transfer.repository;

import com.bank.transfer.model.PhoneTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferPhoneRepository extends JpaRepository<PhoneTransfer, Long> {
}
