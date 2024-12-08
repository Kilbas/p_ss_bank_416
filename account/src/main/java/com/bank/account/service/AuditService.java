package com.bank.account.service;

import com.bank.account.entity.Audit;

public interface AuditService {

    void newAudit(Audit audit);
    Audit findByEntityTypeAndEntityId(String entityType, Long entityId);
}