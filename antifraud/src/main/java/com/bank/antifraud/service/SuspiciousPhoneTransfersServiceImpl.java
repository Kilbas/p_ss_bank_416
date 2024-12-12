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

    private EntityNotFoundException logAndThrowEntityNotFound(Long id, String action) {
        String errorMessage = String.format("Запись с ID %d не найдена. %s невозможно.", id, action);
        log.error(errorMessage);
        return new EntityNotFoundException(errorMessage);
    }

    @Override
    public SuspiciousPhoneTransferDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> logAndThrowEntityNotFound(id,"Поиск "));
    }

    @Override
    public List<SuspiciousPhoneTransferDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SuspiciousPhoneTransferDTO create(SuspiciousPhoneTransferDTO transferDTO) {
        SuspiciousPhoneTransfers entity = mapper.toEntity(transferDTO);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    public SuspiciousPhoneTransferDTO update(Long id, SuspiciousPhoneTransferDTO transferDTO) {
        SuspiciousPhoneTransfers existing = repository.findById(id)
                .orElseThrow(()-> logAndThrowEntityNotFound(id,"Обновление "));
                mapper.updateFromDto(transferDTO, existing);
        repository.save(existing);
        return mapper.toDTO(existing);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw logAndThrowEntityNotFound(id, "Удаление");
        }
        repository.deleteById(id);
    }

    @Override
    public List<SuspiciousPhoneTransferDTO> findTransfersByReason(String reason) {
        return repository.findBySuspiciousReasonContainingIgnoreCase(reason).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
