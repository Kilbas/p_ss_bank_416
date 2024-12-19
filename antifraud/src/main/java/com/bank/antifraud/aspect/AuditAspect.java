package com.bank.antifraud.aspect;

import com.bank.antifraud.entity.Audit;
import com.bank.antifraud.service.AuditService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Aspect
@Slf4j
public class AuditAspect {

    private final AuditService auditService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Метод для обработки создания записи (POST)
    @AfterReturning(value = "execution(* com.bank.antifraud.service.*.create*(..))", returning = "result")
    public void afterCreateTransfer(JoinPoint joinPoint, Object result) {
        if (result == null) {
            log.error("Метод create вернул null. Аудит не будет сохранён.");
            return;
        }

        log.info("Аудит: создание записи");
        saveAudit(result, "CREATE");
    }

    // Метод для обработки обновления записи (PUT)
    @AfterReturning(value = "execution(* com.bank.antifraud.service.*.update*(..))", returning = "result")
    public void afterUpdateTransfer(JoinPoint joinPoint, Object result) {
        if (result == null) {
            log.error("Метод update вернул null. Аудит не будет сохранён.");
            return;
        }

        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || !(args[0] instanceof Long)) {
            log.error("Ошибка: ID не передан или имеет неверный тип.");
            return;
        }

        Long id = (Long) args[0]; // Предполагаем, что ID передается первым аргументом
        String entityType = result.getClass().getSimpleName();

        Audit previousAudit = auditService.findByEntityTypeAndEntityId(entityType, id.toString());

        if (previousAudit == null) {
            log.error("Audit запись не найдена для entityType: {}, entityId: {}", entityType, id);
            throw new EntityNotFoundException(
                    String.format("Audit record not found for entityType: %s, entityId: %s", entityType, id)
            );
        }

        Audit newAudit = new Audit();
        fromSavedAuditToNewAudit(previousAudit, newAudit);

        newAudit.setOperationType("UPDATE");
        newAudit.setModifiedBy("ADMIN");
        newAudit.setModifiedAt(LocalDateTime.now());
        newAudit.setNewEntityJson(convertEntityToJson(result));

        auditService.createAudit(newAudit);
    }

    // Вспомогательный метод для создания новой записи аудита
    private void saveAudit(Object entity, String operationType) {
        try {
            String entityJson = objectMapper.writeValueAsString(entity);
            Audit audit = Audit.builder()
                    .entityType(entity.getClass().getSimpleName())
                    .operationType(operationType)
                    .createdBy("SYSTEM") // Вы можете заменить на текущего пользователя
                    .createdAt(LocalDateTime.now())
                    .entityJson(entityJson)
                    .build();
            auditService.createAudit(audit);
        } catch (JsonProcessingException e) {
            log.error("Ошибка сериализации объекта для аудита: {}", e.getMessage());
        }
    }

    // Вспомогательный метод для копирования данных из предыдущей записи аудита
    private void fromSavedAuditToNewAudit(Audit savedAudit, Audit newAudit) {
        newAudit.setEntityType(savedAudit.getEntityType());
        newAudit.setCreatedBy(savedAudit.getCreatedBy());
        newAudit.setCreatedAt(savedAudit.getCreatedAt());
        newAudit.setEntityJson(savedAudit.getEntityJson());
    }

    // Вспомогательный метод для преобразования объекта в JSON
    private String convertEntityToJson(Object entity) {
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            log.error("Ошибка сериализации объекта для аудита: {}", e.getMessage());
            return null;
        }
    }
}
