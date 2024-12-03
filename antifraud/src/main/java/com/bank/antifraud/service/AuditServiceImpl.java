package com.bank.antifraud.service;

import com.bank.antifraud.dto.AuditDTO;
import com.bank.antifraud.entity.Audit;
import com.bank.antifraud.mapper.AuditMapper;
import com.bank.antifraud.repository.AuditRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class AuditServiceImpl implements AuditService {

    AuditRepository repository;
    AuditMapper mapper;

    public AuditServiceImpl(AuditRepository repository, AuditMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public AuditDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Suspicious account transfer not found with id: " + id));
    }

    @Override
    public List<AuditDTO> findAll() {
       return repository.findAll().stream()
                .map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public AuditDTO create(AuditDTO transferDTO) {
        Audit entity = mapper.toEntity(transferDTO);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    public List<AuditDTO> findByEntityType(String entityType) {
        return repository.findByEntityType(entityType).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<AuditDTO> findByOperationType(String operationType) {
        return repository.findByOperationType(operationType).stream()
                .map(mapper::toDTO)
                .toList();
    }
}
