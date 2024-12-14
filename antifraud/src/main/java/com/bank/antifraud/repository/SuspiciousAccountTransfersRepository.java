package com.bank.antifraud.repository;

import com.bank.antifraud.entity.SuspiciousAccountTransfers;
import com.bank.antifraud.entity.SuspiciousPhoneTransfers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuspiciousAccountTransfersRepository extends JpaRepository<SuspiciousAccountTransfers, Long> {
    List<SuspiciousAccountTransfers> findBySuspiciousReasonContainingIgnoreCase(String reason);
}
