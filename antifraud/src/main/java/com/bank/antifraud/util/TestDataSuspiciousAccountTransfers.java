package com.bank.antifraud.util;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.entity.SuspiciousAccountTransfers;

public class TestDataSuspiciousAccountTransfers {

    public static SuspiciousAccountTransfersDTO createSuspiciousAccountTransfersDTO() {
        return SuspiciousAccountTransfersDTO.builder()
                .id(1L)
                .accountTransferId(100L)
                .isBlocked(true)
                .isSuspicious(true)
                .blockedReason("Fraud detected")
                .suspiciousReason("Large amount transfer")
                .build();
    }

    public static SuspiciousAccountTransfers createSuspiciousAccountTransfersEntity() {
        SuspiciousAccountTransfers entity = new SuspiciousAccountTransfers();
        entity.setId(1L);
        entity.setAccountTransferId(100L);
        entity.setBlocked(true);
        entity.setSuspicious(true);
        entity.setBlockedReason("Fraud detected");
        entity.setSuspiciousReason("Large amount transfer");
        return entity;
    }
}
