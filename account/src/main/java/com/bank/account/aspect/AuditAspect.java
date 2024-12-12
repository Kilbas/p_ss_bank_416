package com.bank.account.aspect;

import com.bank.account.entity.Audit;
import com.bank.account.service.AuditService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class AuditAspect {

    private final AuditService auditService;
    private final ObjectMapper objectMapper;
    private final Timestamp timeStamp = Timestamp.valueOf(LocalDateTime.now());
    private static final String userName = "system";
    private static final String methodSave = "save";
    private static final String methodUpdate = "update";

    @Around("execution(* com.bank.account.service.*Service.save*(..)) || " +
            "execution(* com.bank.account.service.*Service.update*(..))")
    public Object auditLog(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = joinPoint.proceed();
        Audit audit = new Audit();

        String entityType = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String operationType = joinPoint.getSignature().getName();

        audit.setEntityType(entityType);
        audit.setOperationType(operationType);

        if (operationType.startsWith(methodSave)) {
            setCreateAudit(audit, result);
        } else if (operationType.startsWith(methodUpdate)) {
            Long id = extractIdFromArgs(joinPoint.getArgs());
            setUpdateAudit(audit, result);
            updateAudit(id, entityType, audit);
        }

        saveAudit(audit);
        return result;
    }

    private Long extractIdFromArgs(Object[] args) {
        if (args.length > 0 && args[0] instanceof Long) {
            return (Long) args[0];
        } else {
            log.error("Ожидаемый первый аргумент должен быть типа Long");
            throw new IllegalArgumentException("Ожидаемый первый аргумент должен быть типа Long");
        }
    }

    private void setCreateAudit(Audit audit, Object result) throws JsonProcessingException {
        audit.setCreatedBy(userName);
        audit.setCreatedAt(timeStamp);
        audit.setEntityJson(objectMapper.writeValueAsString(result));
    }

    private void setUpdateAudit(Audit audit, Object result) throws JsonProcessingException {
        audit.setModifiedBy(userName);
        audit.setModifiedAt(timeStamp);
        audit.setNewEntityJson(objectMapper.writeValueAsString(result));
    }

    private void updateAudit(Long id, String entityType, Audit audit) {
        Audit oldAudit = auditService.findByEntityTypeAndEntityId(entityType, id);


        audit.setCreatedAt(oldAudit.getCreatedAt());
        audit.setCreatedBy(oldAudit.getCreatedBy());
        audit.setEntityJson(oldAudit.getEntityJson());
    }

    private void saveAudit(Audit audit) {
        auditService.newAudit(audit);
    }
}
