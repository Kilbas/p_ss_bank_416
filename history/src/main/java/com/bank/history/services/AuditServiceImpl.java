package com.bank.history.services;

import com.bank.history.models.Audit;
import com.bank.history.repositories.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Transactional
    @Override
    public void newAudit(Optional<Audit> auditOptional) {
        Audit audit = auditOptional.orElseThrow(() -> new EntityNotFoundException("В метод newAudit передан null!"));
        auditRepository.save(audit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Audit> getAllAudits() {
        return auditRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Audit getAuditByEntityId(String id) {
        return auditRepository.findByEntityId(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Аудит для history с id %s не найден", id)));
    }
}
