package com.bank.publicinfo.aspect;

import com.bank.publicinfo.entity.Audit;
import com.bank.publicinfo.dto.Auditable;
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
import java.util.Arrays;
import java.util.List;

/**
 * Класс для работы с аудитом(сохрание записи в БД).
 */

@Slf4j
@Aspect
@Component
public class AuditAspect {

    private final String login = "Админ";
    private static final String SAVE = "add";
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";

    private final ObjectMapper mapper;                        //Объект для сериализации/десериализации JSON.
    private final AuditService auditService;

    {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * @param auditService Сервис для работы с сущностями Audit.
     */
    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @Pointcut("@annotation(AuditAnnotation)")
    public void callAudit() {
    }

    /**
     * Обрабатывает обновление или удаление записи аудита.
     *
     * @param result    Сущность (DTO).
     * @throws JsonProcessingException Если происходит ошибка сериализации JSON.
     */
    @AfterReturning(pointcut = "callAudit()", returning = "result")
    public void afterReturningAudit(JoinPoint jp, Auditable result) throws JsonProcessingException {

        Audit newAudit = new Audit();
        String signature = jp.getSignature().toString(); //Сигнатура метода, вызвавшего аудит.
                                                         // Используется для определения типа операции и сущности.
        EntityTypeEnum entityType = getEntityType(signature);
        OperationTypeEnum operationType = getOperationType(signature);
        newAudit.setOperationType(operationType.getValue());

        if (entityType == null) {
            log.warn("Не удалось определить тип сущности из подписи: {}", signature);
            return;
        }

        switch (operationType) {
            case CREATE -> addAudit(result, newAudit, entityType);
            case UPDATE, DELETE -> updateOrDeleteAudit(result, jp, newAudit, entityType);
        }
    }

    /**
     * Обрабатывает запись аудита при добавлении сущности в БД.
     *
     * @param result    Добавляемая сущность (DTO).
     * @throws JsonProcessingException Если происходит ошибка сериализации JSON.
     */
    private void addAudit(Auditable result, Audit audit, EntityTypeEnum entityType) throws JsonProcessingException {

        log.info("Попытка записать аудит в БД");
        audit.setCreatedBy(login);
        audit.setCreatedAt(LocalDateTime.now());
        audit.setEntityType(entityType.getValue());
        audit.setEntityJson(mapper.writeValueAsString(result));

        if (StringUtils.hasText(audit.getEntityType())) {
            auditService.addAudit(audit);
            log.info("Запись аудита добавлена успешно");
        }

    }

    /**
     * Обрабатывает запись аудита при обновление или удаление сущности в БД.
     *
     * @param result    Обновляемая или удаляемая сущность (DTO).
     * @throws JsonProcessingException Если происходит ошибка сериализации JSON.
     */
    private void updateOrDeleteAudit(Auditable result, JoinPoint joinPoint, Audit auditUpdate,
                                     EntityTypeEnum entityType) throws JsonProcessingException {

        log.info("Попытка записать аудит с обновленной информацией в БД");
        List<Object> list = Arrays.stream(joinPoint.getArgs()).toList();
        Long id = (Long) list.get(0);
        auditUpdate.setNewEntityJson(mapper.writeValueAsString(result));

        Audit auditFromDB = auditService.findByEntityTypeAndEntityId
                (entityType.getValue(), String.valueOf(id));

        if (auditFromDB != null && StringUtils.hasText(auditUpdate.getNewEntityJson())) {
            auditUpdate.setEntityType(auditFromDB.getEntityType());
            auditUpdate.setCreatedBy(auditFromDB.getCreatedBy());
            auditUpdate.setModifiedBy(login);
            auditUpdate.setCreatedAt(auditFromDB.getCreatedAt());
            auditUpdate.setModifiedAt(LocalDateTime.now());
            auditUpdate.setEntityJson(auditFromDB.getEntityJson());

            auditService.addAudit(auditUpdate);
            log.info(" Запись аудита с обновленной информацией добавлена успешно");
        }
    }

    /**
     * Определяет тип Enum по сигнатуре метода.
     *
     * @param signature Сигнатура метода.
     * @return Тип Enum или null, если тип не найден.
     */
    private EntityTypeEnum getEntityType(String signature) {
        for (EntityTypeEnum type : EntityTypeEnum.values()) {
            if (signature.contains(type.getValue())) {
                return type;
            }
        }
        return null;
    }

    /**
     * Определяет тип операции по сигнатуре метода.
     *
     * @param signature Сигнатура метода.
     * @return Тип операции.
     */
    private OperationTypeEnum getOperationType(String signature) {
        if (signature.contains(SAVE)) return OperationTypeEnum.CREATE;
        if (signature.contains(UPDATE)) return OperationTypeEnum.UPDATE;
        if (signature.contains(DELETE)) return OperationTypeEnum.DELETE;
        return OperationTypeEnum.UNKNOWN;
    }
}
