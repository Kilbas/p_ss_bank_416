package com.bank.history.services;

import com.bank.history.models.Audit;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AuditService {

    void newAudit(Optional<Audit> auditOptional);

    List<Audit> getAllAudits();

    Audit getAuditByEntityId(String id);

}
