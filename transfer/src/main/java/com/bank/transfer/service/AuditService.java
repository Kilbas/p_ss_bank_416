package com.bank.transfer.service;


import com.bank.transfer.model.Audit;

import java.util.List;

public interface AuditService {
    void addAudit(Audit audit);
    List<Audit> getAllAudit();
    Audit getAuditById(long id);
    Audit findByEntityTypeAndEntityId(String entityType, String entityId);
    void updateAudit(Audit audit);
}
