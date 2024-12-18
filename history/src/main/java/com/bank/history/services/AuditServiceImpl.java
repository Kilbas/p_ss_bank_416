package com.bank.history.services;

import com.bank.history.models.Audit;
import com.bank.history.repositories.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Autowired
    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Transactional
    @Override
    public void newAudit(Audit audit) {
        auditRepository.save(audit);
    }

    @Transactional
    @Override
    public List<Audit> getAllAudits() {
        return auditRepository.findAll();
    }

    @Transactional
    @Override
    public Audit getAuditByEntityId(String id) {
        return auditRepository.findByEntityId(id).orElseThrow(() ->
                new EntityNotFoundException("Аудит с id " + id + " не найден."));
    }
}
