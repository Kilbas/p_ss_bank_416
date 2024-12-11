package com.bank.account.service;

import com.bank.account.entity.Audit;
import com.bank.account.repository.AuditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Override
    @Transactional
    public void newAudit(Audit audit) {
        if (audit == null) {
            log.error("Передан null объект для сохранения в Audit");
            throw new IllegalArgumentException("Объект Audit не может быть null");
        }

        try {
            auditRepository.save(audit);
        } catch (DataAccessException e) {
            log.error("Ошибка при сохранении аудита: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при сохранении аудита", e);
        }
    }

    @Override
    public Audit findByEntityTypeAndEntityId(String entityType, Long entityId) {
        if (entityType == null || entityId == null) {
            log.error("Переданы null значения: entityType={}, entityId={}", entityType, entityId);
            throw new IllegalArgumentException("entityType и entityId не могут быть null");
        }

        try {
            Audit audit = auditRepository.findByEntityTypeAndEntityId(entityType, entityId.toString());
            if (audit == null) {
                log.warn("Аудит не найден для entityType={} и entityId={}", entityType, entityId);
                throw new EntityNotFoundException("Аудит не найден для указанных entityType и entityId");
            }
            return audit;
        } catch (DataAccessException e) {
            log.error("Ошибка при поиске аудита: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при поиске аудита", e);
        }
    }
}
