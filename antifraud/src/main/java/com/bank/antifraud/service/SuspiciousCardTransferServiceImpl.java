package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.entity.SuspiciousCardTransfer;
import com.bank.antifraud.mapper.SuspiciousCardTransferMapper;
import com.bank.antifraud.repository.SuspiciousCardTransferRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
public class SuspiciousCardTransferServiceImpl implements SuspiciousCardTransferService {

    private final SuspiciousCardTransferRepository repository;
    private final SuspiciousCardTransferMapper mapper;

    public SuspiciousCardTransferServiceImpl(SuspiciousCardTransferRepository repository,
                                             SuspiciousCardTransferMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    private EntityNotFoundException logAndThrowEntityNotFound(Long id, String action) {
        String errorMessage = String.format("Запись с ID %d не найдена. %s невозможно.", id, action);
        log.error(errorMessage);
        return new EntityNotFoundException(errorMessage);
    }

    @Override
    public SuspiciousCardTransferDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> logAndThrowEntityNotFound(id, "Поиск"));
    }

    @Override
    public List<SuspiciousCardTransferDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SuspiciousCardTransferDTO create(SuspiciousCardTransferDTO transferDTO) {
        SuspiciousCardTransfer entity = mapper.toEntity(transferDTO);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    @Transactional
    public SuspiciousCardTransferDTO update(Long id, SuspiciousCardTransferDTO transferDTO) {
        SuspiciousCardTransfer existing = repository.findById(id)
                .orElseThrow(() -> logAndThrowEntityNotFound(id, "Обновление"));

        mapper.updateFromDto(transferDTO, existing);
        repository.save(existing);
        return mapper.toDTO(existing);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw logAndThrowEntityNotFound(id, "Удаление");
        }
        repository.deleteById(id);
    }

    @Override
    public List<SuspiciousCardTransferDTO> findBlockedTransfers() {
        return repository.findAll().stream()
                .filter(SuspiciousCardTransfer::getIsBlocked)
                .map(mapper::toDTO)
                .toList();
    }
}