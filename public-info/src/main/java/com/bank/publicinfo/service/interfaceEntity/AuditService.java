package com.bank.publicinfo.service.interfaceEntity;

import com.bank.publicinfo.entity.Audit;

public interface AuditService {

    Audit findByEntityTypeAndEntityId(String entityType, String entityId);

    void addAudit(Audit audit);
}