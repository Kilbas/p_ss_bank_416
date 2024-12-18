package com.bank.history.aspects;

import com.bank.history.models.Audit;
import com.bank.history.operations.Operation;
import com.bank.history.services.AuditService;
import com.bank.history.services.HistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@Aspect
@Slf4j
public class AuditAspect {

    private static final String ENTITY = "History";
    private static final String CREATED_WHO = "System";
    private static final String MODIFIED_WHO = "System";
    private final AuditService auditService;
    private final ObjectMapper objectMapper;
    private final HistoryService historyService;

    @Autowired
    public AuditAspect(AuditService auditService, HistoryService historyService) {
        this.auditService = auditService;
        this.historyService = historyService;
        this.objectMapper = new ObjectMapper();
    }

    @Around("execution(com.bank.history.models.History com.bank.history.services.HistoryService.save(com.bank.history.dto.HistoryDTO))")
    public void aroundHistoryServiceSaveAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("aroundHistoryServiceSaveAdvice: Попытка добавить сущность History в базу данных");
        Object result = joinPoint.proceed();
        log.info("aroundHistoryServiceSaveAdvice: сущность History добавлена в базу данных");
        Audit audit = new Audit();
        audit.setOperation(Operation.CREATE);
        audit.setEntityType(ENTITY);
        audit.setCreatedBy(CREATED_WHO);
        audit.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        audit.setNewEntityJson(objectMapper.writeValueAsString(result));
        auditService.newAudit(audit);
    }

    @Around("execution(void com.bank.history.services.HistoryService.update(com.bank.history.dto.HistoryDTO, Long))")
    public void aroundHistoryServiceUpdateAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("aroundHistoryServiceSaveAdvice: Попытка изменить сущность History в базе данных");
        joinPoint.proceed();
        log.info("aroundHistoryServiceSaveAdvice: Сущность History изменена");
        Audit audit = new Audit();
        audit.setOperation(Operation.UPDATE);
        long entityId = 0L;
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Long) {
                entityId = (long) arg;
            }
        }
        String entityIdAsString = String.valueOf(entityId);
        Audit oldAudit = auditService.getAuditByEntityId(entityIdAsString);
        audit.setEntityType(oldAudit.getEntityType());
        audit.setCreatedBy(oldAudit.getCreatedBy());
        audit.setModifiedBy(MODIFIED_WHO);
        audit.setCreatedAt(oldAudit.getCreatedAt());
        audit.setModifiedAt(Timestamp.valueOf(LocalDateTime.now()));
        audit.setNewEntityJson(objectMapper.writeValueAsString(historyService.findById(entityId)));
        oldAudit.setModifiedAt(audit.getModifiedAt());
        oldAudit.setModifiedBy(audit.getModifiedBy());
        oldAudit.setEntityJson(oldAudit.getNewEntityJson());
        oldAudit.setNewEntityJson(objectMapper.writeValueAsString(historyService.findById(entityId)));
        auditService.newAudit(oldAudit);
        auditService.newAudit(audit);
    }

    @Around("execution(void com.bank.history.services.HistoryService.deleteById(Long))")
    public void aroundHistoryServiceDeleteAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Попытка удалить сущность History");
        joinPoint.proceed();
        log.info("Сущность удалена");
        long entityId = (long) joinPoint.getArgs()[0];
        String entityIdAsString = String.valueOf(entityId);
        Audit auditOfCreation = auditService.getAuditByEntityId(entityIdAsString);
        Audit audit = new Audit();
        audit.setOperation(Operation.DELETE);
        audit.setEntityType(ENTITY);
        audit.setCreatedBy(auditOfCreation.getCreatedBy());
        audit.setModifiedBy(MODIFIED_WHO);
        audit.setCreatedAt(auditOfCreation.getCreatedAt());
        audit.setModifiedAt(Timestamp.valueOf(LocalDateTime.now()));
        audit.setNewEntityJson(auditOfCreation.getNewEntityJson());
        auditOfCreation.setModifiedBy(audit.getModifiedBy());
        auditService.newAudit(audit);
        auditService.newAudit(auditOfCreation);
    }

}
