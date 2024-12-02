package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;
import com.bank.antifraud.entity.SuspiciousPhoneTransfers;

public class SuspiciousPhoneTransfersMapper {
    public static SuspiciousPhoneTransferDTO toDTO(SuspiciousPhoneTransfers suspiciousPhoneTransfers) {
        return (suspiciousPhoneTransfers == null) ? null : SuspiciousPhoneTransferDTO.builder()
                .id(suspiciousPhoneTransfers.getPhoneTransferId())
                .phoneTransferId(suspiciousPhoneTransfers.getPhoneTransferId())
                .isBlocked(suspiciousPhoneTransfers.isBlocked())
                .isSuspicious(suspiciousPhoneTransfers.isSuspicious())
                .blockedReason(suspiciousPhoneTransfers.getBlockedReason())
                .suspiciousReason(suspiciousPhoneTransfers.getSuspiciousReason())
                .build();
    }

    public static SuspiciousPhoneTransfers toEntity(SuspiciousPhoneTransferDTO suspiciousPhoneTransferDTO) {
        return (suspiciousPhoneTransferDTO == null) ? null : SuspiciousPhoneTransfers.builder()
                .id(suspiciousPhoneTransferDTO.getId())
                .phoneTransferId(suspiciousPhoneTransferDTO.getPhoneTransferId())
                .isBlocked(suspiciousPhoneTransferDTO.isBlocked())
                .isSuspicious(suspiciousPhoneTransferDTO.isSuspicious())
                .blockedReason(suspiciousPhoneTransferDTO.getBlockedReason())
                .suspiciousReason(suspiciousPhoneTransferDTO.getSuspiciousReason())
                .build();
    }

}
