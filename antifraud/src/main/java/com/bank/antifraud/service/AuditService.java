package com.bank.antifraud.service;


import com.bank.antifraud.entity.Audit;

import java.util.Optional;

public interface AuditService {
    void createAudit(Audit audit);
    public Audit findByEntityTypeAndEntityId(String entityType, String entityId);
}
