package com.bank.transfer.aspect;

import com.bank.transfer.model.Audit;
import com.bank.transfer.model.Auditable;
import com.bank.transfer.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {
    private final AuditService auditService;

    @AfterReturning("execution(* com.bank.transfer.service.*.add*Transfer(*))")
    public void afterAddTransfer(JoinPoint joinPoint) {
        Audit audit = new Audit();
        Auditable auditObject = (Auditable) joinPoint.getArgs()[0];

        audit.setEntityType(auditObject.getClass().getSimpleName());
        audit.setOperationType(joinPoint.getSignature().getName());
        audit.setCreatedBy("User");
        audit.setCreatedAt(LocalDateTime.now());
        audit.setEntityJson(auditObject.toString());

        auditService.addAudit(audit);
    }

    @AfterReturning(value = "execution(* com.bank.transfer.service.*.update*Transfer(*, long))", returning = "result")
    public void afterUpdateTransfer(JoinPoint joinPoint, Auditable result) {
        String entityId =  joinPoint.getArgs()[1].toString();
        String entityType = result.getClass().getSimpleName();

        Audit savedAudit = auditService.findByEntityTypeAndEntityId(entityType, entityId);
        Audit newAudit = new Audit();

        fromSavedAuditToNewAudit(savedAudit, newAudit);

        newAudit.setOperationType(joinPoint.getSignature().getName());
        newAudit.setModifiedBy("Admin");
        newAudit.setModifiedAt(LocalDateTime.now());
        newAudit.setNewEntityJson(result.toString());

        auditService.addAudit(newAudit);
    }

    private void fromSavedAuditToNewAudit (Audit savedAudit, Audit newAudit) {
        newAudit.setEntityType(savedAudit.getEntityType());
        newAudit.setCreatedAt(savedAudit.getCreatedAt());
        newAudit.setCreatedBy(savedAudit.getCreatedBy());
        newAudit.setEntityJson(savedAudit.getEntityJson());
    }
}
