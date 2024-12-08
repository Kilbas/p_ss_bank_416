package com.bank.account.service;

import com.bank.account.entity.Audit;
import com.bank.account.repository.AuditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void newAudit (Audit audit) {
        auditRepository.save(audit);
        log.info("Audit успешно добавлен");
    }

    @Override
    public Audit findByEntityTypeAndEntityId(String entityType, Long entityId) {
        return auditRepository.findByEntityTypeAndEntityId (entityType, entityId.toString());
    }
}
