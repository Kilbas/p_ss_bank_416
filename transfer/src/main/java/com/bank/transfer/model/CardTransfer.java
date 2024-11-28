package com.bank.transfer.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * Представляет операцию перевода, связанную с конкретной банковской картой.
 * <p>
 * Эта сущность соответствует таблице {@code card_transfer} в схеме {@code transfer}.
 * </p>
 */
@Entity
@Table(name = "card_transfer", schema = "transfer")
@Data
@NoArgsConstructor
public class CardTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "card_number", nullable = false, unique = true)
    @Positive(message = "Номер счёта должен быть положительным")
    @NotNull
    private long number;

    @Column(name = "amount", nullable = false)
    @DecimalMin(value = "0.01", message = "Сумма перевода должна быть больше 0.01")
    @Digits(integer = 20, fraction = 2, message = "Сумма перевода должна содержать не более 20 цифр")
    @NotNull
    private BigDecimal amount;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "account_details_id", nullable = false)
    @NotNull
    @Positive(message = "ID должен быть положительным")
    private long accountDetailsId;

    public CardTransfer(long number, BigDecimal amount, String purpose, long accountDetailsId) {
        this.number = number;
        this.amount = amount;
        this.purpose = purpose;
        this.accountDetailsId = accountDetailsId;
    }
}
