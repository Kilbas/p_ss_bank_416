package com.bank.antifraud.repository;

import com.bank.antifraud.entity.SuspiciousAccountTransfers;
import io.micrometer.core.annotation.Timed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuspiciousAccountTransfersRepository extends JpaRepository<SuspiciousAccountTransfers, Long> {

    @Timed
    List<SuspiciousAccountTransfers> findBySuspiciousReasonContainingIgnoreCase(String reason);


    @Timed
    List<SuspiciousAccountTransfers> findByBlockedTrue();

    @Timed
    List<SuspiciousAccountTransfers> findBySuspiciousTrue();
}
