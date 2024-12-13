package com.bank.publicinfo.service.audit;

import com.bank.publicinfo.entity.Audit;
import com.bank.publicinfo.repository.AuditRepository;
import com.bank.publicinfo.service.interfaceEntity.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Transactional
@Service
public class AuditServiceImp implements AuditService {

    private final AuditRepository auditRepository;

    public AuditServiceImp(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Audit findByEntityTypeAndEntityId(String entityType, String entityId) {
        return auditRepository.findByEntityTypeAndEntityId(entityType, entityId).orElseThrow(() -> {
            log.error("Не найдена запись аудита с типом сущности {} и/или id сущности {}", entityType, entityId);
            return new EntityNotFoundException(
                    String.format("Не найдена запись аудита с типом сущности %s и айди сущности %s", entityType, entityId));
        });
    }

    @Override
    public void addAudit(Audit audit) {
        auditRepository.save(audit);
    }
}