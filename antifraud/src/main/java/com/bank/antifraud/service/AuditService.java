package com.bank.antifraud.service;


import com.bank.antifraud.entity.Audit;

public interface AuditService {
    void createAudit(Audit audit);
    Audit findByEntityTypeAndEntityId(String entityType, String entityId);
}
