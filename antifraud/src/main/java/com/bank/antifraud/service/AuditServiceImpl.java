package com.bank.antifraud.service;

import com.bank.antifraud.dto.AuditDTO;
import com.bank.antifraud.entity.Audit;
import com.bank.antifraud.mapper.AuditMapper;
import com.bank.antifraud.repository.AuditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@AllArgsConstructor
@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository repository;


    @Override
    @Transactional
    public void newAudit (Audit audit) {
        repository.save(audit);
    }

    @Override
    public Audit findByEntityTypeAndEntityId(String entityType, Long entityId) {
        return repository.findByEntityTypeAndEntityId (entityType, entityId.toString());
    }




}
