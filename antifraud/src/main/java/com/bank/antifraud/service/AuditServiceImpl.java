package com.bank.antifraud.service;

import com.bank.antifraud.entity.Audit;
import com.bank.antifraud.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository repository;

    @Override
    public void addAudit(Audit audit) {
        repository.save(audit);
    }


}
