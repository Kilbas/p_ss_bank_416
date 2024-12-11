package com.bank.antifraud.service;

import com.bank.antifraud.dto.AuditDTO;
import com.bank.antifraud.entity.Audit;

import java.util.List;


public interface AuditService {

    void addAudit(Audit audit);
    Audit findByEntityTypeAndEntityId(String entityType, Long entityId);
}
