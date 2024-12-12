package com.bank.transfer.aspect;

import com.bank.transfer.model.Audit;
import com.bank.transfer.model.Auditable;
import com.bank.transfer.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final String ADMIN_ROLE = "ADMIN";
    private final String USER_ROLE = "USER";
    private final AuditService auditService;

    @AfterReturning(value = "execution(* com.bank.transfer.service.*.add*Transfer(*))", returning = "result")
    public void afterAddTransfer(JoinPoint joinPoint, Auditable result) {
        Audit audit = new Audit();

        audit.setEntityType(result.getClass().getSimpleName());
        audit.setOperationType(joinPoint.getSignature().getName());
        audit.setCreatedBy(USER_ROLE);
        audit.setCreatedAt(LocalDateTime.now());
        audit.setEntityJson(result.toString());

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
        newAudit.setModifiedBy(ADMIN_ROLE);
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
