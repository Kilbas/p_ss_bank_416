package com.bank.transfer.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "phone_transfer", schema = "transfer")
@Data
public class PhoneTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "phone_number", nullable = false)
    @Positive(message = "Номер счёта должен быть положительным")
    private long number;

    @Column(name = "amount", nullable = false)
    @DecimalMin(value = "0.01", message = "Сумма перевода должна быть больше 0.01")
    @Digits(integer = 20, fraction = 2, message = "Сумма перевода должна содержать не более 20 цифр")
    private BigDecimal amount;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "account_details_id", nullable = false)
    @Positive(message = "ID должен быть положительным")
    private long accountDetailsId;
}
