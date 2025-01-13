package com.bank.antifraud.util;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;
import com.bank.antifraud.entity.SuspiciousPhoneTransfers;

public class TestDataSuspiciousPhoneTransfers {

    public static SuspiciousPhoneTransferDTO createSuspiciousPhoneTransfersDTO() {
        return SuspiciousPhoneTransferDTO.builder()
                .id(1L)
                .phoneTransferId(100L)
                .isBlocked(true)
                .isSuspicious(true)
                .blockedReason("Fraud detected")
                .suspiciousReason("Large amount transfer")
                .build();
    }

    public static SuspiciousPhoneTransfers createSuspiciousPhoneTransfersEntity() {
        SuspiciousPhoneTransfers entity = new SuspiciousPhoneTransfers();
        entity.setId(1L);
        entity.setPhoneTransferId(100L);
        entity.setBlocked(true);
        entity.setSuspicious(true);
        entity.setBlockedReason("Fraud detected");
        entity.setSuspiciousReason("Large amount transfer");
        return entity;
    }
}
