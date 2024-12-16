package com.bank.antifraud.service;

import com.bank.antifraud.entity.Audit;
import com.bank.antifraud.repository.AuditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@AllArgsConstructor
@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository repository;


    @Override
    @Transactional
    public void addAudit (Audit audit) {
        repository.save(audit);
    }

    @Override
    public Audit findByEntityTypeAndEntityId(String entityType, Long entityId) {
        return repository.findByEntityTypeAndEntityId (entityType, entityId.toString());
    }




}
