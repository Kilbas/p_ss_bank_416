package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.entity.SuspiciousCardTransfer;
import com.bank.antifraud.mapper.SuspiciousCardTransferMapper;
import com.bank.antifraud.repository.SuspiciousCardTransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuspiciousCardTransferServiceImpl implements SuspiciousCardTransferService {

    private static final Logger log = LoggerFactory.getLogger(SuspiciousCardTransferServiceImpl.class);

    private final SuspiciousCardTransferRepository repository;
    private final SuspiciousCardTransferMapper mapper;

    public SuspiciousCardTransferServiceImpl(SuspiciousCardTransferRepository repository,
                                             SuspiciousCardTransferMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public SuspiciousCardTransferDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> {
                    log.error("Перевод с ID {} не найден", id);
                    return new EntityNotFoundException("Перевод не найден с ID: " + id);
                });
    }

    @Override
    public List<SuspiciousCardTransferDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SuspiciousCardTransferDTO create(SuspiciousCardTransferDTO transferDTO) {
        SuspiciousCardTransfer entity = mapper.toEntity(transferDTO);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    public SuspiciousCardTransferDTO update(Long id, SuspiciousCardTransferDTO transferDTO) {

        SuspiciousCardTransfer existing = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Перевод с ID {} не найден для обновления", id);
                    return new IllegalArgumentException("Перевод не найден с ID: " + id);
                });

        mapper.updateFromDto(transferDTO, existing);
        repository.save(existing);
        return mapper.toDTO(existing);
    }



    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            log.error("Перевод с ID {} не найден для удаления", id);
            throw new EntityNotFoundException("Перевод не найден с ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<SuspiciousCardTransferDTO> findBlockedTransfers() {
        log.info("Запрос на получение всех заблокированных переводов");
        return repository.findAll().stream()
                .filter(SuspiciousCardTransfer::getIsBlocked)
                .map(mapper::toDTO)
                .toList();
    }
}