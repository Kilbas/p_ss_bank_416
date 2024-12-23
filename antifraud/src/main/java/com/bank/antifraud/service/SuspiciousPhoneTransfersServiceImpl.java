package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;
import com.bank.antifraud.entity.SuspiciousPhoneTransfers;
import com.bank.antifraud.mapper.SuspiciousPhoneTransfersMapper;
import com.bank.antifraud.repository.SuspiciousPhoneTransfersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
public class SuspiciousPhoneTransfersServiceImpl implements SuspiciousPhoneTransfersService {

    private final SuspiciousPhoneTransfersRepository repository;
    private final SuspiciousPhoneTransfersMapper mapper;

    public SuspiciousPhoneTransfersServiceImpl(SuspiciousPhoneTransfersRepository repository,
                                               SuspiciousPhoneTransfersMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public SuspiciousPhoneTransferDTO findByIdPhoneTransfers(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Запись с ID %d не найдена поиск невозможен", id)));
    }

    @Override
    public List<SuspiciousPhoneTransferDTO> findAllPhoneTransfers() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public SuspiciousPhoneTransferDTO createNewPhoneTransfers(SuspiciousPhoneTransferDTO transferDTO) {
        SuspiciousPhoneTransfers entity = mapper.toEntity(transferDTO);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Transactional
    @Override
    public SuspiciousPhoneTransferDTO updatePhoneTransfers(Long id, SuspiciousPhoneTransferDTO transferDTO) {
        SuspiciousPhoneTransfers existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Запись с ID %d не найдена. Обновление невозможно.", id)));


        if (!existing.getPhoneTransferId().equals(transferDTO.getPhoneTransferId())) {
            throw new IllegalArgumentException("Поле phoneTransferId не может быть изменено.");
        }
        mapper.updateFromDto(transferDTO, existing);
        repository.save(existing);
        return mapper.toDTO(existing);
    }

    @Transactional
    @Override
    public void deletePhoneTransfers(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(String.format("Запись с ID %d не найдена. Удаление невозможно.", id));
        }
        repository.deleteById(id);
    }

    @Override
    public List<SuspiciousPhoneTransferDTO> findTransfersByReason(String reason) {
        return repository.findBySuspiciousReasonContainingIgnoreCase(reason).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SuspiciousPhoneTransferDTO> findBlockedTransfers() {
        return repository.findByBlockedTrue().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<SuspiciousPhoneTransferDTO> findSuspiciousTransfers() {
        return repository.findBySuspiciousTrue().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
