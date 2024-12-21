package com.bank.antifraud.service;


import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.entity.SuspiciousCardTransfer;
import com.bank.antifraud.mapper.SuspiciousCardTransferMapper;
import com.bank.antifraud.repository.SuspiciousCardTransferRepository;
import lombok.extern.slf4j.Slf4j;
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


    @Override
    public SuspiciousCardTransferDTO findByIdCardTransfer(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Запись с ID %d не найдена поиск невозможен", id)));
    }

    @Override
    public List<SuspiciousCardTransferDTO> findAllCardTransfers() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SuspiciousCardTransferDTO createNewCardTransfer(SuspiciousCardTransferDTO transferDTO) {
        SuspiciousCardTransfer entity = mapper.toEntity(transferDTO);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Transactional
    public SuspiciousCardTransferDTO updateCardTransfer(Long id, SuspiciousCardTransferDTO transferDTO) {
        SuspiciousCardTransfer existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Запись с ID %d не найдена. Обновление невозможно.", id)));

        // Проверяем, что cardTransferId не изменяется
        if (!existing.getCardTransferId().equals(transferDTO.getCardTransferId())) {
            throw new IllegalArgumentException("Поле cardTransferId не может быть изменено.");
        }

        mapper.updateFromDto(transferDTO, existing);
        repository.save(existing);
        return mapper.toDTO(existing);
    }


    @Override
    @Transactional
    public void deleteCardTransfer(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(String.format("Запись с ID %d не найдена. Удаление невозможно.", id));
        }
        repository.deleteById(id);
    }

    @Override
    public List<SuspiciousCardTransferDTO> findTransfersByReason(String reason) {
        return repository.findBySuspiciousReasonContainingIgnoreCase(reason).stream()
                .map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<SuspiciousCardTransferDTO> findBlockedTransfers() {
        return repository.findAll().stream()
                .filter(SuspiciousCardTransfer::isBlocked)
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<SuspiciousCardTransferDTO> findSuspiciousTransfers() {
        return repository.findAll().stream()
                .filter(SuspiciousCardTransfer::isSuspicious)
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

}