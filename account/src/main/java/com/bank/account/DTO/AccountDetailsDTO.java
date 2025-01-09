package com.bank.account.DTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@EqualsAndHashCode
public class AccountDetailsDTO {

    Long id;

    @NotNull(message = "Поле passportId не может быть пустым")
    @Min(value = 1, message = "Минимальное значение 1")
    Long passportId;

    @NotNull(message = "Поле accountNumber не может быть пустым")
    Long accountNumber;

    @NotNull(message = "Поле bankDetailsId не может быть пустым")
    Long bankDetailsId;

    @NotNull(message = "Поле money не может быть пустым")
    @Digits(integer = 20, fraction = 2, message = "Не корректная сумма денег")
    BigDecimal money;

    @NotNull(message = "Поле negativeBalance е может быть пустым")
    Boolean negativeBalance;

    @NotNull(message = "Поле profileId не может быть пустым")
    Long profileId;
}
