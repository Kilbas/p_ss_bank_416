package com.bank.publicinfo.aspect;

import com.bank.publicinfo.dto.Auditable;
import com.bank.publicinfo.entity.Audit;
import com.bank.publicinfo.service.audit.AuditService;
import com.bank.publicinfo.util.enums.EntityTypeEnum;
import com.bank.publicinfo.util.enums.OperationTypeEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class AuditAspect {

    private final String login = "Админ";
    private final LocalDateTime timeNow = LocalDateTime.now();
    private static final String SAVE = "add";
    private static final String UPDATE = "update";

    private final ObjectMapper mapper;
    private final AuditService auditService;

    {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @Pointcut("@annotation(AuditAnnotation)")
    public void callAudit() {
    }

    @AfterReturning(pointcut = "callAudit()", returning = "result")
    public void afterReturningAudit(JoinPoint jp, Auditable result) throws JsonProcessingException, UnsupportedOperationException {

        Audit newAudit = new Audit();
        String signature = jp.getSignature().toString();
        EntityTypeEnum entityType = getEntityType(signature);
        OperationTypeEnum operationType = getOperationType(signature);
        newAudit.setOperationType(operationType.getValue());

        switch (operationType) {
            case CREATE -> addAudit(result, newAudit, entityType);
            case UPDATE -> updateOrDeleteAudit(result, jp, newAudit, entityType);
        }
    }

    private void addAudit(Auditable result, Audit audit, EntityTypeEnum entityType) throws JsonProcessingException {

        audit.setCreatedBy(login);
        audit.setCreatedAt(timeNow);
        audit.setEntityType(entityType.getValue());
        log.warn("Может быть выброшено исключение при маппинге в столбец таблицы EntityJson");
        audit.setEntityJson(mapper.writeValueAsString(result));

        if (StringUtils.hasText(audit.getEntityType())) {
            auditService.addAudit(audit);
        }
    }

    private void updateOrDeleteAudit(Auditable result, JoinPoint joinPoint, Audit auditUpdate,
                                     EntityTypeEnum entityType) throws JsonProcessingException {

        auditUpdate.setNewEntityJson(mapper.writeValueAsString(result));
        log.warn("Может быть выброшено исключение при маппинге в столбец таблицы NewEntityJson");

        if (joinPoint.getArgs().length < 1 || !(joinPoint.getArgs()[0] instanceof Long)) {
            throw new IllegalArgumentException("Ожидаемый первый аргумент должен быть типа Long");
        }

        String id = String.valueOf(joinPoint.getArgs()[0]);
        Audit auditFromDB = auditService.findByEntityTypeAndEntityId(entityType.getValue(), id);

        if (StringUtils.hasText(auditUpdate.getNewEntityJson())) {
            auditUpdate.setEntityType(auditFromDB.getEntityType());
            auditUpdate.setCreatedBy(auditFromDB.getCreatedBy());
            auditUpdate.setModifiedBy(login);
            auditUpdate.setCreatedAt(auditFromDB.getCreatedAt());
            auditUpdate.setModifiedAt(timeNow);
            auditUpdate.setEntityJson(auditFromDB.getEntityJson());

            auditService.addAudit(auditUpdate);
        }
    }

    private EntityTypeEnum getEntityType(String signature) {
        for (EntityTypeEnum type : EntityTypeEnum.values()) {
            if (signature.contains(type.getValue())) {
                return type;
            }
        }
        log.error("Не удалось определить тип сущности");
        throw new UnsupportedOperationException("Не удалось определить тип сущности");
    }

    private OperationTypeEnum getOperationType(String signature) {
        if (signature.contains(SAVE)) {
            return OperationTypeEnum.CREATE;
        } else if (signature.contains(UPDATE)) {
            return OperationTypeEnum.UPDATE;
        } else {
            log.error("Неизвестный тип опперации при попытке записать в Audit");
            throw new UnsupportedOperationException("Не удалось определить тип опперации над сущностью");
        }
    }
}