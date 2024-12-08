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
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Сущность "Audit" для хранения информации об операциях в системе.
 * Представляет данные аудита для отслеживания изменений сущностей.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "audit", schema = "anti_fraud")
public class Audit {

    /**
     * Уникальный идентификатор аудита.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Тип сущности, к которой относится аудит.
     * Например, "User", "Transaction".
     */
    @Column(name = "entity_Type", length = 40, nullable = false)
    private String entityType;

    /**
     * Тип операции, связанной с сущностью.
     * Например, "CREATE", "UPDATE", "DELETE".
     */
    @Column(name = "operation_type", length = 255, nullable = false)
    private String operationType;

    /**
     * Пользователь, который создал эту запись.
     */
    @Column(name = "created_by", length = 255, nullable = false)
    private String createdBy;

    /**
     * Пользователь, который изменил запись (если применимо).
     */
    @Column(name = "modified_by", length = 255)
    private String modifiedBy;

    /**
     * Дата и время создания аудита.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Дата и время последнего изменения аудита.
     */
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    /**
     * JSON-представление обновленной сущности.
     * Используется для отслеживания изменений в данных.
     */
    @Column(name = "new_entity_json", columnDefinition = "text")
    private String newEntityJson;

    /**
     * JSON-представление оригинальной сущности до изменений.
     */
    @Column(name = "entity_json", columnDefinition = "text")
    private String entityJson;

    /**
     * Переопределенный метод equals для сравнения объектов аудита.
     * Учитывает прокси-классы Hibernate для корректной работы.
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
        Audit audit = (Audit) o;
        return getId() != null && Objects.equals(getId(), audit.getId());
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
