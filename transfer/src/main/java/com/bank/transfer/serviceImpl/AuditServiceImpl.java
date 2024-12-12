package com.bank.transfer.serviceImpl;

import com.bank.transfer.model.Audit;
import com.bank.transfer.repository.AuditRepository;
import com.bank.transfer.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuditServiceImpl implements AuditService {
    private final AuditRepository auditRepository;

    @Transactional(readOnly = true)
    public Audit findByEntityTypeAndEntityId(String entityType, String entityId) {
        Audit audit = auditRepository.findByEntityTypeAndEntityId(entityType, entityId);

        if (audit == null) {
            log.error("не найден аудит с типом сущности {} и айди сущности {}", entityType, entityId);
            throw new EntityNotFoundException(
                    String.format("не найден аудит с типом сущности %s и айди сущности %s", entityType, entityId)
            );
        }

        return auditRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Override
    public void addAudit(Audit audit) {
        auditRepository.save(audit);
    }
}
