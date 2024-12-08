package com.bank.account.aspect;

import com.bank.account.entity.Audit;
import com.bank.account.service.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class AuditAspect {

    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    @Around("execution(* com.bank.account.service.*Service.save*(..)) || " +
            "execution(* com.bank.account.service.*Service.update*(..)) || " +
            "execution(* com.bank.account.service.*Service.delete*(..))")
    public Object auditLog(ProceedingJoinPoint joinPoint) throws Throwable {

        final String userName = "system";
        Object result = joinPoint.proceed();
        Long id;
        Audit audit = new Audit();

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String entityType = method.getReturnType().getSimpleName();
        String operationType = method.getName();

        log.info("audit.setEntityType {}", entityType);
        audit.setEntityType(entityType);

        log.info("audit.setOperationType {}", operationType);
        audit.setOperationType(operationType);

        if (operationType.startsWith("save")) {

            log.info("audit.setCreatedBy {}", userName);
            audit.setCreatedBy(userName);

            log.info("audit.setCreatedAt {}", Timestamp.valueOf(LocalDateTime.now()));
            audit.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

            log.info("audit.setEntityJson {}", result);
            audit.setEntityJson(objectMapper.writeValueAsString(result));

        } else if (operationType.startsWith("update")) {

            id = (Long) joinPoint.getArgs()[0];

            log.info("audit.setModifiedAt {}", Timestamp.valueOf(LocalDateTime.now()));
            audit.setModifiedAt(Timestamp.valueOf(LocalDateTime.now()));

            log.info("audit.setModifiedBy {}", userName);
            audit.setModifiedBy(userName);

            log.info("audit.setNewEntityJson {}", result);
            audit.setNewEntityJson(objectMapper.writeValueAsString(result));

            updateAudit(id, entityType, audit);

        } else if (operationType.startsWith("delete")) {

            id = (Long) joinPoint.getArgs()[0];

            log.info("audit.setModifiedAt {}", Timestamp.valueOf(LocalDateTime.now()));
            audit.setModifiedAt(Timestamp.valueOf(LocalDateTime.now()));

            log.info("audit.setModifiedBy {}", userName);
            audit.setModifiedBy(userName);

            log.info("audit.setNewEntityJson {}", "null");
            audit.setNewEntityJson(null);

            updateAudit(id, entityType, audit);
        }

        auditService.newAudit(audit);

        return result;
    }

    private void updateAudit(Long id, String entityType, Audit audit) {

        Audit oldAudit = auditService.findByEntityTypeAndEntityId(entityType, id);

        log.info("audit.setCreatedAt {}", oldAudit.getCreatedAt());
        audit.setCreatedAt(oldAudit.getCreatedAt());

        log.info("audit.setModifiedBy {}", oldAudit.getCreatedBy());
        audit.setCreatedBy(oldAudit.getCreatedBy());

        log.info("audit.setEntityJson {}", oldAudit.getEntityJson());
        audit.setEntityJson(oldAudit.getEntityJson());
    }
}