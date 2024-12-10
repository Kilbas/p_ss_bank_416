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
    public SuspiciousPhoneTransferDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> {
                    log.error("Подозрительный перевод с ID {} не найден.", id);
                    return new IllegalArgumentException("Перевод с ID " + id + " не найден.");
                });
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
                .orElseThrow(() -> {
                    log.error("Подозрительный перевод с ID {} не найден.", id);
                    return new IllegalArgumentException("Перевод с ID " + id + " не найден.");
                });
        mapper.updateFromDto(transferDTO, existing);
        repository.save(existing);
        return mapper.toDTO(existing);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            log.error("Подозрительный перевод с ID {} не найден. Удаление невозможно.", id);
            throw new EntityNotFoundException("Перевод с ID " + id + " не найден.");
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
