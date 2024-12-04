package com.bank.transfer.service;

import com.bank.transfer.model.Audit;
import com.bank.transfer.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuditServiceImpl implements AuditService {
    private final AuditRepository auditRepository;

    @Transactional(readOnly = true)
    public Audit findByEntityTypeAndEntityId(String entityType, String entityId) {
        return auditRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Override
    @Transactional
    public void addAudit(Audit audit) {
        auditRepository.save(audit);
        log.info("Audit успешно добавлен");
    }

    @Override
    @Transactional
    public void updateAudit(Audit audit) {
        auditRepository.save(audit);
        log.info("обновлен audit с id {}", audit.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Audit> getAllAudit() {
        log.info("Получены все Audit");

        return auditRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Audit getAuditById(long id) {
        log.info("Поиск Audit c id {}", id);

        return auditRepository.findById(id).
                orElseThrow(() -> logAndThrowEntityNotFoundException(id));
    }

    private EntityNotFoundException logAndThrowEntityNotFoundException(long id) {
        log.error("Не найден Audit с указанным id {}", id);

        return new EntityNotFoundException("Не найден Audit с id" + id);
    }
}
