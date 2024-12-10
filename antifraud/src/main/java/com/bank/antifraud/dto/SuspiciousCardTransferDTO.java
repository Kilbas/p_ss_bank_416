package com.bank.antifraud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuspiciousCardTransferDTO {
    private Long id; // ID может быть null при создании новой записи

    @NotNull(message = "Phone cardTransferId ID cannot be null")
    private Long cardTransferId;

    @NotNull(message = "Blocked status cannot be null")
    private Boolean isBlocked;

    @NotNull(message = "Suspicious status cannot be null")
    private Boolean isSuspicious;

    @Size(max = 500, message = "Blocked reason must not exceed 500 characters")
    private String blockedReason;

    @NotBlank(message = "Suspicious reason cannot be blank")
    @Size(max = 500, message = "Suspicious reason must not exceed 500 characters")
    private String suspiciousReason;


}
