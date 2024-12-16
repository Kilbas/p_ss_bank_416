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

    private Long id;

    @NotNull(message = "Идентификатор перевода не может быть пустым")
    private long accountTransferId;

    @NotNull(message = "Статус блокировки не может быть пустым")
    private boolean isBlocked;

    @NotNull(message = "Статус подозрительности не может быть пустым")
    private boolean isSuspicious;

    @Size(max = 255, message = "Причина блокировки не должна превышать 255 символов")
    private String blockedReason;

    @NotBlank(message = "Причина подозрительности не может быть пустой")
    @Size(max = 255, message = "Причина подозрительности не должна превышать 255 символов")
    private String suspiciousReason;

}
