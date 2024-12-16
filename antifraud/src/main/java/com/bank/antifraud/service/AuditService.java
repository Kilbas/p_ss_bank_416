package com.bank.antifraud.service;

import com.bank.antifraud.entity.Audit;


public interface AuditService {

    void addAudit(Audit audit);
    Audit findByEntityTypeAndEntityId(String entityType, Long entityId);
}
