package com.bank.antifraud.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Сущность "SuspiciousCardTransfer" для представления данных
 * о подозрительных переводах с использованием карты.
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "suspicious_card_transfer", schema = "anti_fraud")
public class SuspiciousCardTransfer {

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор перевода по карте.
     * Поле должно быть уникальным и не может быть null.
     */
    @Column(name = "card_transfer_id", nullable = false, unique = true)
    private Long cardTransferId;

    /**
     * Статус блокировки перевода.
     * true - перевод заблокирован, false - нет.
     */
    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;

    /**
     * Статус подозрительности перевода.
     * true - перевод является подозрительным, false - нет.
     */
    @Column(name = "is_suspicious", nullable = false)
    private boolean isSuspicious;

    /**
     * Причина блокировки перевода.
     * Может быть null.
     */
    @Column(name = "blocked_reason", columnDefinition = "TEXT")
    private String blockedReason;

    /**
     * Причина, по которой перевод был помечен как подозрительный.
     * Может быть null.
     */
    @Column(name = "suspicious_reason", columnDefinition = "TEXT")
    private String suspiciousReason;

    /**
     * Переопределенный метод equals для сравнения объектов.
     * Учитывает использование Hibernate-прокси для корректной работы.
     *
     * @param o объект для сравнения.
     * @return true, если объекты равны, иначе false.
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        SuspiciousCardTransfer that = (SuspiciousCardTransfer) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    /**
     * Переопределенный метод hashCode для корректной работы хэш-таблиц.
     *
     * @return хэш-код объекта.
     */
    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
