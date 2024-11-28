package com.bank.transfer.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit", schema = "transfer")
@Data
@NoArgsConstructor
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "entity_type")
    @Size(max = 40)
    private String entityType;

    @Column(name = "operation_type")
    @Size(max = 255)
    private String operationType;

    @Column(name = "created_by")
    @Size(max = 255)
    private String createdBy;

    @Column(name = "modified_by", nullable = false)
    @Size(max = 255)
    private String modifiedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "new_entity_json")
    private String newEntityJson;

    @Column(name = "entity_json", nullable = false)
    private String entityJson;

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
