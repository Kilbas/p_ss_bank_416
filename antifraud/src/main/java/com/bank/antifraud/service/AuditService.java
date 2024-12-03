package com.bank.antifraud.service;

import com.bank.antifraud.dto.AuditDTO;

import java.util.List;


public interface AuditService {

    AuditDTO findById(Long id);
    List<AuditDTO> findAll();
    AuditDTO create(AuditDTO dto);
    List<AuditDTO> findByEntityType(String entityType); // Найти записи аудита по типу сущности
    List<AuditDTO> findByOperationType(String operationType); // Найти записи по типу операции
}
