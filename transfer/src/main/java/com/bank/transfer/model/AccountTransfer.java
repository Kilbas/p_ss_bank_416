package com.bank.transfer.model;

import com.bank.transfer.aspect.Auditable;
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
 * Представляет операцию перевода, связанную с конкретным банковским счётом.
 * <p>
 * Эта сущность соответствует таблице {@code account_transfer} в схеме {@code transfer}.
 * Содержит информацию о номере счёта, сумме перевода, цели перевода и
 * связанных деталях счёта.
 * </p>
 * <p>
 * Для обеспечения целостности данных применяются аннотации валидации,
 * такие как проверка на положительное значение номера счёта и суммы,
 * а также ограничение масштаба и точности суммы перевода.
 * </p>
 */
@Entity
@Table(name = "account_transfer", schema = "transfer")
@NoArgsConstructor
@Data
public class AccountTransfer implements Auditable {

    /**
     * Уникальный идентификатор операции перевода.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    /**
     * Уникальный номер счёта, с которого выполняется перевод.
     * <p>
     * Должен быть положительным числом.
     * </p>
     */
    @Column(name = "account_number", nullable = false, unique = true)
    @Positive(message = "Номер счёта должен быть положительным")
    @NotNull
    private long number;

    /**
     * Сумма денег, подлежащая переводу.
     * <p>
     * Должна быть больше 0.01 и содержать не более 20 цифр и 2 знака после запятой.
     * </p>
     */
    @Column(name = "amount", nullable = false)
    @DecimalMin(value = "0.01", message = "Сумма перевода должна быть больше 0.01")
    @Digits(integer = 20, fraction = 2, message = "Сумма перевода должна содержать не более 20 цифр")
    @NotNull
    private BigDecimal amount;

    /**
     * Цель или описание перевода (необязательное поле).
     */
    @Column(name = "purpose")
    private String purpose;

    /**
     * Идентификатор деталей счёта, связанных с переводом.
     * <p>
     * Должен быть положительным числом.
     * </p>
     */
    @Column(name = "account_details_id", nullable = false)
    @Positive(message = "ID должен быть положительным")
    @NotNull
    private long accountDetailsId;

    /**
     * Конструктор для создания объекта {@code AccountTransfer} с указанными значениями.
     *
     * @param number           уникальный номер счёта, с которого выполняется перевод
     * @param amount           сумма денег, подлежащая переводу
     * @param purpose          цель или описание перевода
     * @param accountDetailsId идентификатор связанных деталей счёта
     */
    public AccountTransfer(long number, BigDecimal amount, String purpose, long accountDetailsId) {
        this.number = number;
        this.amount = amount;
        this.purpose = purpose;
        this.accountDetailsId = accountDetailsId;
    }

    @Override
    public String toString() {
        return "{\"id\":" + id +
                ",\"number\":" + number +
                ",\"amount\":" + amount +
                ",\"purpose\":\"" + purpose + '\"' +
                ",\"accountDetailsId\":" + accountDetailsId +
                '}';
    }
}
