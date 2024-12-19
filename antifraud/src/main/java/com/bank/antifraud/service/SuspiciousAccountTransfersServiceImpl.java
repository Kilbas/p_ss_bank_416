package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.entity.SuspiciousAccountTransfers;
import com.bank.antifraud.mapper.SuspiciousAccountTransfersMapper;
import com.bank.antifraud.repository.SuspiciousAccountTransfersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SuspiciousAccountTransfersServiceImpl implements SuspiciousAccountTransfersService {

    private final SuspiciousAccountTransfersRepository repository;
    private final SuspiciousAccountTransfersMapper mapper;



    private EntityNotFoundException logAndThrowEntityNotFound(Long id, String action) {
        String errorMessage = String.format("Запись с ID %d не найдена. %s невозможно.", id, action);
        log.error(errorMessage);
        return new EntityNotFoundException(errorMessage);
    }

    @Override
    @Transactional
    public SuspiciousAccountTransfersDTO createNewAccountTransfer(SuspiciousAccountTransfersDTO transferDTO) {
        SuspiciousAccountTransfers entity = mapper.toEntity(transferDTO);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    @Transactional
    public SuspiciousAccountTransfersDTO updateAccountTransfer(Long id, SuspiciousAccountTransfersDTO transferDTO) {
        SuspiciousAccountTransfers existing = repository.findById(id)
                .orElseThrow(() -> logAndThrowEntityNotFound(id, "Обновление"));
        mapper.updateFromDto(transferDTO, existing);
         repository.save(existing);
        return mapper.toDTO(existing);
    }

    @Override
    @Transactional
    public void deleteAccountTransfer(Long id) {
        if (!repository.existsById(id)) {
            throw logAndThrowEntityNotFound(id, "Удаление");
        }
        repository.deleteById(id);
    }

    @Override
    public SuspiciousAccountTransfersDTO findByIdAccountTransfer(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> logAndThrowEntityNotFound(id, "Поиск"));
    }

    @Override
    public List<SuspiciousAccountTransfersDTO> findAllAccountTransfers() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

    }

    @Override
    public List<SuspiciousAccountTransfersDTO> findTransfersByReason(String reason) {
        return repository.findBySuspiciousReasonContainingIgnoreCase(reason).stream()
                .map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<SuspiciousAccountTransfersDTO> findBlockedTransfers() {
        return repository.findAll().stream()
                .filter(SuspiciousAccountTransfers::isBlocked)
                .map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<SuspiciousAccountTransfersDTO> findSuspiciousTransfers() {
        return repository.findAll().stream()
                .filter(SuspiciousAccountTransfers::isSuspicious)
                .map(mapper::toDTO).collect(Collectors.toList());
    }

}