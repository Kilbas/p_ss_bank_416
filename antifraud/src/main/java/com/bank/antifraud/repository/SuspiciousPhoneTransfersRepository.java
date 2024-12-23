package com.bank.antifraud.repository;

import com.bank.antifraud.entity.SuspiciousPhoneTransfers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuspiciousPhoneTransfersRepository extends JpaRepository<SuspiciousPhoneTransfers, Long> {

    List<SuspiciousPhoneTransfers> findBySuspiciousReasonContainingIgnoreCase(String reason);

    List<SuspiciousPhoneTransfers> findByBlockedTrue();

    List<SuspiciousPhoneTransfers> findBySuspiciousTrue();
}
