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
public class SuspiciousAccountTransfersDTO {
    private Long id;
    private Long accountTransferId;
    private Boolean isBlocked;
    private Boolean isSuspicious;
    private String blockedReason;
    private String suspiciousReason;
}
