package com.bank.history.services;

import com.bank.history.models.Audit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuditService {

    void newAudit(Audit audit);

    List<Audit> getAllAudits();

    Audit getAuditByEntityId(String id);

}
