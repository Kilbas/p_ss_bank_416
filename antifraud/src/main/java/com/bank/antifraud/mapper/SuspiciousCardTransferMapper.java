package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.entity.SuspiciousCardTransfer;

public class SuspiciousCardTransferMapper {
   public static SuspiciousCardTransferDTO toDTO (SuspiciousCardTransfer suspiciousCardTransfer){
       return (suspiciousCardTransfer == null) ? null : SuspiciousCardTransferDTO.builder()
               .id(suspiciousCardTransfer.getId())
               .cardTransferId(suspiciousCardTransfer.getCardTransferId())
               .isBlocked(suspiciousCardTransfer.getIsBlocked())
               .isSuspicious(suspiciousCardTransfer.getIsSuspicious())
               .blockedReason(suspiciousCardTransfer.getBlockedReason())
               .suspiciousReason(suspiciousCardTransfer.getSuspiciousReason())
               .build();
   }

   public static SuspiciousCardTransfer toEntity (SuspiciousCardTransferDTO suspiciousCardTransferDTO){
       return (suspiciousCardTransferDTO == null) ? null : SuspiciousCardTransfer.builder()
               .id(suspiciousCardTransferDTO.getId())
               .cardTransferId(suspiciousCardTransferDTO.getCardTransferId())
               .isBlocked(suspiciousCardTransferDTO.getIsBlocked())
               .isSuspicious(suspiciousCardTransferDTO.getIsSuspicious())
               .blockedReason(suspiciousCardTransferDTO.getBlockedReason())
               .suspiciousReason(suspiciousCardTransferDTO.getSuspiciousReason())
               .build();
   }

}
