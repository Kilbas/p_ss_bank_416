package com.bank.transfer.service;

import com.bank.transfer.model.Audit;
import com.bank.transfer.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuditServiceImpl implements AuditService {
    private final AuditRepository auditRepository;

    @Transactional(readOnly = true)
    public Audit findByEntityTypeAndEntityId(String entityType, String entityId) {
        return auditRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Override
    @Transactional
    public void addAudit(Audit audit) {
        auditRepository.save(audit);
        log.info("Audit успешно добавлен");
    }
}
