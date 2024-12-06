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
public class SuspiciousAccountTransfersDTO {

    private Long id; // ID может быть null при создании новой записи

   @NotNull(message = "Account transfer ID cannot be null")
    private long accountTransferId;

   @NotNull(message = "Blocked status cannot be null")
    private boolean isBlocked;

   @NotNull(message = "Suspicious status cannot be null")
    private boolean isSuspicious;

    //@NotBlank(message = "Blocked reason cannot be blank")
    @Size(max = 255, message = "Blocked reason must not exceed 255 characters")
    private String blockedReason;

    @NotBlank(message = "Suspicious reason cannot be blank")
    @Size(max = 255, message = "Suspicious reason must not exceed 255 characters")
    private String suspiciousReason;

}
