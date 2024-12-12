package com.bank.account.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "audit", schema = "account")
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "entity_type", nullable = false, length = 40)
    String entityType;

    @Column(name = "operation_type", nullable = false)
    String operationType;

    @Column(name = "created_by", nullable = false)
    String createdBy;

    @Column(name = "modified_by")
    String modifiedBy;

    @Column(name = "created_at", nullable = false)
    Timestamp createdAt;

    @Column(name = "modified_at")
    Timestamp modifiedAt;

    @Column(name = "new_entity_json", columnDefinition = "TEXT")
    String newEntityJson;

    @Column(name = "entity_json", columnDefinition = "TEXT", nullable = false)
    String entityJson;
}
