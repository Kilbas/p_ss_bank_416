package com.bank.transfer.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Представляет сущность для аудита изменений в системе.
 * <p>
 * Эта сущность хранит информацию о типе сущности, операции, инициаторе изменений,
 * времени создания и модификации, а также о состоянии сущности до и после изменений.
 * </p>
 * <p>
 * Эта сущность соответствует таблице {@code audit} в схеме {@code transfer}.
 * </p>
 */
@Entity
@Table(name = "audit", schema = "transfer")
@Data
@NoArgsConstructor
public class Audit {
    /**
     * Уникальный идентификатор записи аудита.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /**
     * Тип сущности, для которой создаётся запись аудита.
     * <p>
     * Например: "AccountTransfer", "CardTransfer".
     * Максимальная длина — 40 символов.
     * </p>
     */
    @Column(name = "entity_type")
    @Size(max = 40)
    private String entityType;

    /**
     * Тип операции, которая была выполнена над сущностью.
     * <p>
     * Максимальная длина — 255 символов.
     * </p>
     */
    @Column(name = "operation_type")
    @Size(max = 255)
    private String operationType;

    /**
     * Пользователь или процесс, создавший эту запись аудита.
     * <p>
     * Максимальная длина — 255 символов.
     * </p>
     */
    @Column(name = "created_by")
    @Size(max = 255)
    private String createdBy;

    /**
     * Пользователь или процесс, изменивший запись.
     * <p>
     * Обязательное поле. Максимальная длина — 255 символов.
     * </p>
     */
    @Column(name = "modified_by")
    @Size(max = 255)
    private String modifiedBy;

    /**
     * Дата и время создания записи.
     * <p>
     * Обязательное поле.
     * </p>
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Дата и время последней модификации записи.
     */
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    /**
     * JSON-строка, представляющая новое состояние сущности после изменений.
     */
    @Column(name = "new_entity_json")
    private String newEntityJson;

    /**
     * JSON-строка, представляющая предыдущее состояние сущности перед изменениями.
     * <p>
     * Обязательное поле.
     * </p>
     */
    @Column(name = "entity_json", nullable = false)
    private String entityJson;

    /**
     * Конструктор для создания объекта {@code Audit} с указанными значениями.
     *
     * @param entityType    тип сущности, для которой создаётся запись
     * @param operationType тип выполненной операции
     * @param createdBy     пользователь, создавший запись
     * @param modifiedBy    пользователь, изменивший запись
     * @param createdAt     дата и время создания записи
     * @param modifiedAt    дата и время модификации записи
     * @param newEntityJson JSON-строка нового состояния сущности
     * @param entityJson    JSON-строка предыдущего состояния сущности
     */
    public Audit(String entityType, String operationType, String createdBy,
                 String modifiedBy, LocalDateTime createdAt, LocalDateTime modifiedAt,
                 String newEntityJson, String entityJson) {
        this.entityType = entityType;
        this.operationType = operationType;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.newEntityJson = newEntityJson;
        this.entityJson = entityJson;
    }
}
