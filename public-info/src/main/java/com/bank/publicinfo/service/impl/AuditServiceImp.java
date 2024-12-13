package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.entity.Audit;
import com.bank.publicinfo.repository.AuditRepository;
import com.bank.publicinfo.service.interfaceEntity.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuditServiceImp implements AuditService {

    private String errorMessage;
    private final AuditRepository auditRepository;

    @Override
    @Transactional(readOnly = true)
    public Audit findByEntityTypeAndEntityId(String entityType, String entityId) {
        if (entityType == null || entityId == null) {
            errorMessage = "Передаваемые параметры entityType и или entityId не могут быть null";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        return auditRepository.findByEntityTypeAndEntityId(entityType, entityId).orElseThrow(() -> {
            errorMessage = String.format("Не найдена запись аудита с типом сущности %s и айди сущности %s", entityType, entityId);
            log.error(errorMessage);
            return new EntityNotFoundException(errorMessage);
        });
    }

    @Override
    public void addAudit(@Valid Audit audit) {
        try {
            auditRepository.save(audit);
        } catch (DataAccessException e) {
            errorMessage = "Ошибка при сохранении аудита: " + e.getMessage();
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }
}