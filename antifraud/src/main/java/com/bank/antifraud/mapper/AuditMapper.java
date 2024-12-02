package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.AuditDTO;
import com.bank.antifraud.entity.Audit;

public class AuditMapper {
    public static AuditDTO toAuditDTO(Audit audit) {
        return (audit == null) ? null : AuditDTO.builder()
                .id(audit.getId())
                .entityType(audit.getEntityType())
                .operationType(audit.getOperationType())
                .createdBy(audit.getCreatedBy())
                .modifiedBy(audit.getModifiedBy())
                .createdAt(audit.getCreatedAt())
                .modifiedAt(audit.getModifiedAt())
                .newEntityJson(audit.getNewEntityJson())
                .entityJson(audit.getEntityJson())
                .build();
    }

    public static Audit toAudit(AuditDTO auditDTO) {
        return (auditDTO == null) ? null : Audit.builder()
                .id(auditDTO.getId())
                .entityType(auditDTO.getEntityType())
                .operationType(auditDTO.getOperationType())
                .createdBy(auditDTO.getCreatedBy())
                .modifiedBy(auditDTO.getModifiedBy())
                .createdAt(auditDTO.getCreatedAt())
                .modifiedAt(auditDTO.getModifiedAt())
                .newEntityJson(auditDTO.getNewEntityJson())
                .entityJson(auditDTO.getEntityJson())
                .build();
    }
}
