package com.bank.antifraud.controller;

import com.bank.antifraud.dto.AuditDTO;
import com.bank.antifraud.service.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/audits")
public class AuditController {

    private static final Logger logger = LoggerFactory.getLogger(AuditController.class);

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Получение аудита по ID.
     *
     * @param id ID аудита.
     * @return Ответ с данными аудита.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuditDTO> getById(@PathVariable("id") Long id) {
        logger.info("Получен запрос на получение аудита по ID: {}", id);
        if (id == null || id <= 0) {
            logger.warn("Некорректный ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        try {
            AuditDTO audit = auditService.findById(id);
            logger.info("Аудит найден: {}", audit);
            return ResponseEntity.ok(audit);
        } catch (Exception e) {
            logger.error("Ошибка при получении аудита по ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Получение всех аудитов.
     *
     * @return Список всех аудитов.
     */
    @GetMapping
    public ResponseEntity<List<AuditDTO>> getAll() {
        logger.info("Получен запрос на получение всех аудитов.");
        List<AuditDTO> audits = auditService.findAll();
        if (audits.isEmpty()) {
            logger.info("Список аудитов пуст.");
            return ResponseEntity.noContent().build();
        }
        logger.info("Возвращен список аудитов, количество записей: {}", audits.size());
        return ResponseEntity.ok(audits);
    }

    /**
     * Создание нового аудита.
     *
     * @param auditDTO Данные для создания аудита.
     * @return Созданный аудит.
     */
    @PostMapping
    public ResponseEntity<AuditDTO> create(@Valid @RequestBody AuditDTO auditDTO) {
        logger.info("Получен запрос на создание аудита: {}", auditDTO);
        if (auditDTO == null) {
            logger.warn("Данные для создания аудита пусты.");
            return ResponseEntity.badRequest().build();
        }
        try {
            AuditDTO createdAudit = auditService.create(auditDTO);
            logger.info("Аудит успешно создан: {}", createdAudit);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAudit);
        } catch (Exception e) {
            logger.error("Ошибка при создании аудита.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Получение аудитов по типу сущности.
     *
     * @param entityType Тип сущности.
     * @return Список аудитов по типу сущности.
     */
    @GetMapping("/entity-type/{entityType}")
    public ResponseEntity<List<AuditDTO>> findByEntityType(@PathVariable("entityType") String entityType) {
        logger.info("Получен запрос на поиск аудитов по типу сущности: {}", entityType);
        if (entityType == null || entityType.isEmpty()) {
            logger.warn("Некорректный тип сущности: {}", entityType);
            return ResponseEntity.badRequest().build();
        }
        List<AuditDTO> audits = auditService.findByEntityType(entityType);
        if (audits.isEmpty()) {
            logger.info("Аудиты с типом сущности '{}' не найдены.", entityType);
            return ResponseEntity.noContent().build();
        }
        logger.info("Найдены аудиты по типу сущности '{}', количество: {}", entityType, audits.size());
        return ResponseEntity.ok(audits);
    }

    /**
     * Получение аудитов по типу операции.
     *
     * @param operationType Тип операции.
     * @return Список аудитов по типу операции.
     */
    @GetMapping("/operation-type/{operationType}")
    public ResponseEntity<List<AuditDTO>> findByOperationType(@PathVariable("operationType") String operationType) {
        logger.info("Получен запрос на поиск аудитов по типу операции: {}", operationType);
        if (operationType == null || operationType.isEmpty()) {
            logger.warn("Некорректный тип операции: {}", operationType);
            return ResponseEntity.badRequest().build();
        }
        List<AuditDTO> audits = auditService.findByOperationType(operationType);
        if (audits.isEmpty()) {
            logger.info("Аудиты с типом операции '{}' не найдены.", operationType);
            return ResponseEntity.noContent().build();
        }
        logger.info("Найдены аудиты по типу операции '{}', количество: {}", operationType, audits.size());
        return ResponseEntity.ok(audits);
    }
}
