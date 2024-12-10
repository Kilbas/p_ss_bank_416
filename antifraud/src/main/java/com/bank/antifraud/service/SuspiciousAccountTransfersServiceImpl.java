package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.entity.SuspiciousAccountTransfers;
import com.bank.antifraud.mapper.SuspiciousAccountTransfersMapper;
import com.bank.antifraud.repository.SuspiciousAccountTransfersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SuspiciousAccountTransfersServiceImpl implements SuspiciousAccountTransfersService {


    private final SuspiciousAccountTransfersRepository repository;
    private final SuspiciousAccountTransfersMapper mapper;

    @Autowired
    public SuspiciousAccountTransfersServiceImpl(SuspiciousAccountTransfersRepository repository,
                                                 SuspiciousAccountTransfersMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public SuspiciousAccountTransfersDTO create(SuspiciousAccountTransfersDTO transferDTO) {
        SuspiciousAccountTransfers entity = mapper.toEntity(transferDTO);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    public SuspiciousAccountTransfersDTO update(Long id, SuspiciousAccountTransfersDTO transferDTO) {
        SuspiciousAccountTransfers existing = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Запись с ID {} не найдена.", id);
                    return new IllegalArgumentException("Запись с ID " + id + " не найдена.");
                });
        mapper.updateFromDto(transferDTO, existing);
        SuspiciousAccountTransfers updatedEntity = repository.save(existing);
        return mapper.toDTO(updatedEntity);
    }


    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            log.error("Запись с ID {} не найдена. Удаление невозможно.", id);
            throw new IllegalArgumentException("Запись с ID " + id + " не найдена.");
        }
        repository.deleteById(id);
    }

    @Override
    public SuspiciousAccountTransfersDTO findById(Long id) {

        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> {
                    log.error("Запись с ID {} не найдена.", id);
                    return new IllegalArgumentException("Запись с ID " + id + " не найдена.");
                });
    }

    @Override
    public List<SuspiciousAccountTransfersDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
