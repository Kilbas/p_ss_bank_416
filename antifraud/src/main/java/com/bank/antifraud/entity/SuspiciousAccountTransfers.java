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
 * Сущность "SuspiciousAccountTransfers" для представления данных
 * о подозрительных банковских переводах.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Suspicious_account_transfers", schema = "anti_fraud")
public class SuspiciousAccountTransfers {

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор банковского перевода, связанного с записью.
     * Должен быть уникальным.
     */
    @Column(name = "account_transfer_id", nullable = false, unique = true)
    private long accountTransferId;

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
    @Column(name = "is_Suspicious", nullable = false, columnDefinition = "text")
    private boolean isSuspicious;

    /**
     * Причина блокировки перевода (если применимо).
     * Может быть null.
     */
    @Column(name = "blocked_reason", nullable = true, columnDefinition = "text")
    private String blockedReason;

    /**
     * Причина, по которой перевод был помечен как подозрительный.
     */
    @Column(name = "suspicious_reason", nullable = false)
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
        SuspiciousAccountTransfers that = (SuspiciousAccountTransfers) o;
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
