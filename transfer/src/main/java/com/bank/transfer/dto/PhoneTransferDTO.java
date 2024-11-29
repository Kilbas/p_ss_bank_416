package com.bank.transfer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PhoneTransferDTO {

    @Positive(message = "Номер счёта должен быть положительным")
    @NotNull
    private long number;

    @DecimalMin(value = "0.01", message = "Сумма перевода должна быть больше 0.01")
    @Digits(integer = 20, fraction = 2, message = "Сумма перевода должна содержать не более 20 цифр")
    @NotNull
    private BigDecimal amount;

    private String purpose;

    @Positive(message = "ID должен быть положительным")
    @NotNull
    private long accountDetailsId;

    public PhoneTransferDTO(long number, BigDecimal amount, String purpose, long accountDetailsId) {
        this.number = number;
        this.amount = amount;
        this.purpose = purpose;
        this.accountDetailsId = accountDetailsId;
    }
}
