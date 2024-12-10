package com.bank.publicinfo.service.audit;

import com.bank.publicinfo.entity.Audit;
import com.bank.publicinfo.repository.AuditRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuditServiceImp implements AuditService {

    private final AuditRepository auditRepository;

    public AuditServiceImp(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Audit findByEntityTypeAndEntityId(String entityType, String entityId) {
        return auditRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Override
    @Transactional
    public void addAudit(Audit audit) {
        auditRepository.save(audit);
    }
}