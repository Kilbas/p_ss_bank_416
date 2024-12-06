package com.bank.transfer.service;


import com.bank.transfer.model.Audit;


public interface AuditService {
    void addAudit(Audit audit);
    Audit findByEntityTypeAndEntityId(String entityType, String entityId);
}
