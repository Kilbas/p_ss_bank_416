package com.bank.antifraud.serviceImpl;

import com.bank.antifraud.entity.Audit;
import com.bank.antifraud.repository.AuditRepository;
import com.bank.antifraud.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Transactional(readOnly = true)
    @Override
    public Audit findByEntityTypeAndEntityId(String entityType, String entityId) {

        if (entityType == null || entityType.isEmpty() || entityId == null || entityId.isEmpty()) {
            throw new EntityNotFoundException("entityType или entityId не может быть null или пустой строкой");
        }

        Audit audit = auditRepository.findByEntityTypeAndEntityId(entityType, entityId);

        if (audit == null) {
            log.error("Не найден аудит с типом сущности {} и ID сущности {}", entityType, entityId);
            throw new EntityNotFoundException(
                    String.format("Не найден аудит с типом сущности %s и ID сущности %s", entityType, entityId)
            );
        }

        return audit;
    }

    @Override
    public void createAudit(Audit audit) {
        auditRepository.save(audit);
    }
}
