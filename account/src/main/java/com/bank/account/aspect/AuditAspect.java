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
    private final String userName = "system";
    private final String methodSave = "save";
    private final String methodUpdate = "update";

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
            setCreateAudit(audit, userName, result);
        } else if (operationType.startsWith(methodUpdate)) {
            Long id = extractIdFromArgs(joinPoint.getArgs());
            setUpdateAudit(audit, userName, result);
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

    private void setCreateAudit(Audit audit, String userName, Object result) throws JsonProcessingException {
        audit.setCreatedBy(userName);
        audit.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        audit.setEntityJson(objectMapper.writeValueAsString(result));
    }

    private void setUpdateAudit(Audit audit, String userName, Object result) throws JsonProcessingException {
        audit.setModifiedBy(userName);
        audit.setModifiedAt(Timestamp.valueOf(LocalDateTime.now()));
        audit.setNewEntityJson(objectMapper.writeValueAsString(result));
    }

    private void updateAudit(Long id, String entityType, Audit audit) {
        Audit oldAudit = auditService.findByEntityTypeAndEntityId(entityType, id);

        if (oldAudit == null) {
            log.error("Аудит для сущности {} с id {} не найден", entityType, id);
            return;
        }

        audit.setCreatedAt(oldAudit.getCreatedAt());
        audit.setCreatedBy(oldAudit.getCreatedBy());
        audit.setEntityJson(oldAudit.getEntityJson());
    }

    private void saveAudit(Audit audit) {
        try {
            auditService.newAudit(audit);
        } catch (Exception ex) {
            log.error("Ошибка при сохранении аудита", ex);
        }
    }
}
