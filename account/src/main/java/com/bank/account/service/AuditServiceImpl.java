package com.bank.account.service;

import com.bank.account.entity.Audit;
import com.bank.account.repository.AuditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new IllegalArgumentException("Объект Audit не может быть null");
        }

        try {
            auditRepository.save(audit);
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка при сохранении аудита", e);
        }
    }

    @Override
    public Audit findByEntityTypeAndEntityId(String entityType, Long entityId) {
        if (entityType == null || entityId == null) {
            throw new IllegalArgumentException("entityType и или entityId не могут быть null");
        }

        try {
            Audit audit = auditRepository.findByEntityTypeAndEntityId(entityType, entityId.toString());
            if (audit == null) {
                throw new RuntimeException(String.format("Аудит не найден для entityType = %s и entityId = %s", entityType, entityId));
            }
            return audit;
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка при поиске аудита", e);
        }
    }
}
