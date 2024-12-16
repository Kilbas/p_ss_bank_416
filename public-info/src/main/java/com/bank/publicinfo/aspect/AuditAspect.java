package com.bank.publicinfo.aspect;

import com.bank.publicinfo.dto.Auditable;
import com.bank.publicinfo.entity.Audit;
import com.bank.publicinfo.service.interfaceEntity.AuditService;
import com.bank.publicinfo.util.enums.EntityTypeEnum;
import com.bank.publicinfo.util.enums.OperationTypeEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class AuditAspect {

    private String errorMessage;
    private final String login = "ADMIN";
    private static final String OPERATION_SAVE = "add";
    private static final String OPERATION_UPDATE = "update";

    private final ObjectMapper mapper;
    private final AuditService auditService;

    @Pointcut("@annotation(AuditAnnotation)")
    public void callAudit() {
    }

    @AfterReturning(pointcut = "callAudit()", returning = "result")
    public void afterReturningAudit(JoinPoint jp, Auditable result) throws JsonProcessingException, UnsupportedOperationException {
        if (jp == null || result == null) {
            errorMessage = "Передаваемые параметры JoinPoint и или Auditable не могут быть null";
            log.error(errorMessage);
            throw new NullPointerException(errorMessage);
        }

        Audit newAudit = new Audit();
        String signature = jp.getSignature().getName();
        EntityTypeEnum entityType = getEntityType(signature);
        OperationTypeEnum operationType = getOperationType(signature);

        newAudit.setOperationType(operationType.getValue());

        switch (operationType) {
            case CREATE -> addAudit(result, newAudit, entityType);
            case UPDATE -> updateAudit(result, newAudit, findAuditBD(jp, entityType));
        }
    }

    private void addAudit(Auditable result, Audit audit, EntityTypeEnum entityType) throws JsonProcessingException {

        audit.setCreatedBy(login);
        audit.setCreatedAt(LocalDateTime.now());
        audit.setEntityType(entityType.getValue());
        log.warn("Может быть выброшено исключение при маппинге в столбец таблицы EntityJson");
        audit.setEntityJson(mapper.writeValueAsString(result));

        if (StringUtils.hasText(audit.getEntityType())) {
            auditService.addAudit(audit);
        }
    }

    private void updateAudit(Auditable result, Audit auditUpdate, Audit auditFromDB) throws JsonProcessingException {

        auditUpdate.setNewEntityJson(mapper.writeValueAsString(result));
        log.warn("Может быть выброшено исключение при маппинге в столбец таблицы NewEntityJson");

        if (StringUtils.hasText(auditUpdate.getNewEntityJson())) {
            auditUpdate.setEntityType(auditFromDB.getEntityType());
            auditUpdate.setCreatedBy(auditFromDB.getCreatedBy());
            auditUpdate.setModifiedBy(login);
            auditUpdate.setCreatedAt(auditFromDB.getCreatedAt());
            auditUpdate.setModifiedAt(LocalDateTime.now());
            auditUpdate.setEntityJson(auditFromDB.getEntityJson());

            auditService.addAudit(auditUpdate);
        }
    }

    private Audit findAuditBD(JoinPoint joinPoint, EntityTypeEnum entityType) {
        if (joinPoint.getArgs().length < 1 || !(joinPoint.getArgs()[0] instanceof Long)) {
            errorMessage = "Ожидаемый первый аргумент должен быть типа Long";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        String id = String.valueOf(joinPoint.getArgs()[0]);
        return auditService.findByEntityTypeAndEntityId(entityType.getValue(), id);
    }

    private EntityTypeEnum getEntityType(String signature) {
        for (EntityTypeEnum type : EntityTypeEnum.values()) {
            if (signature.contains(type.getValue())) {
                return type;
            }
        }
        errorMessage = String.format("Не удалось определить тип сущности при попытке записать в Audit: %s", signature);
        log.error(errorMessage);
        throw new UnsupportedOperationException(errorMessage);
    }

    private OperationTypeEnum getOperationType(String signature) {
        Map<String, OperationTypeEnum> operationMap = Map.of(
                OPERATION_SAVE, OperationTypeEnum.CREATE,
                OPERATION_UPDATE, OperationTypeEnum.UPDATE
        );

        return operationMap.entrySet().stream()
                .filter(entry -> signature.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> {
                    errorMessage = String.format(
                            "Не удалось определить тип операции над сущностью при попытке записать в Audit: %s", signature);
                    log.error(errorMessage);
                    return new UnsupportedOperationException(errorMessage);
                });
    }
}