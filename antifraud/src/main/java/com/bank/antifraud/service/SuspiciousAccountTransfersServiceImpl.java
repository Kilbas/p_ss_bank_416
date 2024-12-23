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
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Запись с ID %d не найдена, обновление  невозможен", id)));

        if (!existing.getAccountTransferId().equals(transferDTO.getAccountTransferId())) {
            throw new IllegalArgumentException("Поле accountTransferId не может быть изменено.");
        }
        mapper.updateFromDto(transferDTO, existing);
        repository.save(existing);
        return mapper.toDTO(existing);
    }

    @Override
    @Transactional
    public void deleteAccountTransfer(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(String.format("Запись с ID %d не найдена. Удаление невозможно.", id));
        }
        repository.deleteById(id);
    }

    @Override
    public SuspiciousAccountTransfersDTO findByIdAccountTransfer(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Запись с ID %d не найдена поиск невозможен", id)));
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
        return repository.findByBlockedTrue().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SuspiciousAccountTransfersDTO> findSuspiciousTransfers() {
        return repository.findBySuspiciousTrue().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

}