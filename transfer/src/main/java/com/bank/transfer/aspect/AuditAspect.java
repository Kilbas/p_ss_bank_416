package com.bank.transfer.aspect;

import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.model.Audit;
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

        audit.setEntityType(joinPoint.getArgs()[0].getClass().getSimpleName());
        audit.setOperationType(joinPoint.getSignature().getName());
        audit.setCreatedBy("Anton");
        audit.setModifiedBy("Anton");
        audit.setCreatedAt(LocalDateTime.now());
        audit.setModifiedAt(null);
        audit.setNewEntityJson(null);
        audit.setEntityJson(joinPoint.getArgs()[0].toString());

        auditService.addAudit(audit);
    }

    @AfterReturning(value = "execution(* com.bank.transfer.service.*.update*Transfer(..))")
    public void afterUpdateTransfer(JoinPoint joinPoint) {
        Audit audit = new Audit();

        audit.setOperationType(joinPoint.getSignature().getName());
        audit.setModifiedBy("Anton");
        audit.setModifiedAt(LocalDateTime.now());
        audit.setNewEntityJson(null);

    }
}
