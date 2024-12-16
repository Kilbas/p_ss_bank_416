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

    private Long id;

    @NotNull(message = "Идентификатор перевода по карте не может быть пустым")
    private Long cardTransferId;

    @NotNull(message = "Статус блокировки не может быть пустым")
    private Boolean isBlocked;

    @NotNull(message = "Статус подозрительности не может быть пустым")
    private Boolean isSuspicious;

    @Size(max = 500, message = "Причина блокировки не должна превышать 500 символов")
    private String blockedReason;

    @NotBlank(message = "Причина подозрительности не может быть пустой")
    @Size(max = 500, message = "Причина подозрительности не должна превышать 500 символов")
    private String suspiciousReason;

}
