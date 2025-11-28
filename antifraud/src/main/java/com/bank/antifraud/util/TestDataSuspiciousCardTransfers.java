package com.bank.antifraud.util;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.entity.SuspiciousCardTransfer;

public class TestDataSuspiciousCardTransfers {

    public static SuspiciousCardTransferDTO createSuspiciousCardTransfersDTO() {
        return SuspiciousCardTransferDTO.builder()
                .id(1L)
                .cardTransferId(100L)
                .isBlocked(true)
                .isSuspicious(true)
                .blockedReason("Fraud detected")
                .suspiciousReason("Large amount transfer")
                .build();
    }

    public static SuspiciousCardTransfer createSuspiciousCardTransfersEntity() {
        SuspiciousCardTransfer entity = new SuspiciousCardTransfer();
        entity.setId(1L);
        entity.setCardTransferId(100L);
        entity.setBlocked(true);
        entity.setSuspicious(true);
        entity.setBlockedReason("Fraud detected");
        entity.setSuspiciousReason("Large amount transfer");
        return entity;
    }
}
