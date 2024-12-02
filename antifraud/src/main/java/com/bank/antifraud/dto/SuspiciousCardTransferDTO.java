package com.bank.antifraud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuspiciousCardTransferDTO {
    private Long id;
    private Long cardTransferId;
    private Boolean isBlocked;
    private Boolean isSuspicious;
    private String blockedReason;
    private String suspiciousReason;


}
