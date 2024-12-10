package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.entity.SuspiciousAccountTransfers;
import com.bank.antifraud.mapper.SuspiciousAccountTransfersMapper;
import com.bank.antifraud.repository.SuspiciousAccountTransfersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
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

    private EntityNotFoundException logAndThrowEntityNotFound(Long id, String action) {
        String errorMessage = String.format("Запись с ID %d не найдена. %s невозможно.", id, action);
        log.error(errorMessage);
        return new EntityNotFoundException(errorMessage);
    }


    @Override
    @Transactional
    public SuspiciousAccountTransfersDTO create(SuspiciousAccountTransfersDTO transferDTO) {
        SuspiciousAccountTransfers entity = mapper.toEntity(transferDTO);
        entity = repository.save(entity);
        log.info("Создана новая запись с ID {}.", entity.getId());
        return mapper.toDTO(entity);
    }

    @Override
    @Transactional
    public SuspiciousAccountTransfersDTO update(Long id, SuspiciousAccountTransfersDTO transferDTO) {
        SuspiciousAccountTransfers existing = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Запись с ID {} не найдена. Обновление невозможно.", id);
                    return new EntityNotFoundException(String.format("Запись с ID %d не найдена. Обновление невозможно.", id));
                });
        mapper.updateFromDto(transferDTO, existing);
        SuspiciousAccountTransfers updatedEntity = repository.save(existing);
        log.info("Запись с ID {} успешно обновлена.", id);
        return mapper.toDTO(updatedEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            logAndThrowEntityNotFound(id, "Удаление");
        }
        repository.deleteById(id);
        log.info("Запись с ID {} успешно удалена.", id);
    }

    @Override
    public SuspiciousAccountTransfersDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> logAndThrowEntityNotFound(id, "Поиск"));
    }

    @Override
    public List<SuspiciousAccountTransfersDTO> findAll() {
        List<SuspiciousAccountTransfersDTO> result = repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        log.info("Найдено {} записей.", result.size());
        return result;
    }
}
