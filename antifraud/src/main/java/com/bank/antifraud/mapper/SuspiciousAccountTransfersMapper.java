package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.entity.SuspiciousAccountTransfers;

public class SuspiciousAccountTransfersMapper {
    public static SuspiciousAccountTransfersDTO toDTO(SuspiciousAccountTransfers suspiciousAccountTransfers) {
        return (suspiciousAccountTransfers == null) ? null : SuspiciousAccountTransfersDTO.builder()
                .id(suspiciousAccountTransfers.getId())
                .accountTransferId(suspiciousAccountTransfers.getAccountTransferId())
                .isBlocked(suspiciousAccountTransfers.isBlocked())
                .isSuspicious(suspiciousAccountTransfers.isSuspicious())
                .blockedReason(suspiciousAccountTransfers.getBlockedReason())
                .suspiciousReason(suspiciousAccountTransfers.getSuspiciousReason())
                .build();
    }

    public static SuspiciousAccountTransfers toEntity(SuspiciousAccountTransfersDTO suspiciousAccountTransferDTO) {
        return (suspiciousAccountTransferDTO == null) ? null : SuspiciousAccountTransfers.builder()
                .id(suspiciousAccountTransferDTO.getId())
                .accountTransferId(suspiciousAccountTransferDTO.getAccountTransferId())

                .isBlocked(suspiciousAccountTransferDTO.getIsBlocked())
                .isSuspicious(suspiciousAccountTransferDTO.getIsSuspicious())
                .blockedReason(suspiciousAccountTransferDTO.getBlockedReason())
                .suspiciousReason(suspiciousAccountTransferDTO.getSuspiciousReason())
                .build();
    }

}
