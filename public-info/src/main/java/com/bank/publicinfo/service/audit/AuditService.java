package com.bank.publicinfo.service.audit;

import com.bank.publicinfo.entity.Audit;

public interface AuditService {

    Audit findByEntityTypeAndEntityId(String entityType, String entityId);

    void addAudit(Audit audit);
}