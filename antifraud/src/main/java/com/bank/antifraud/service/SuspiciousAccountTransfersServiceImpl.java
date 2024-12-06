package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.entity.SuspiciousAccountTransfers;
import com.bank.antifraud.mapper.SuspiciousAccountTransfersMapper;
import com.bank.antifraud.repository.SuspiciousAccountTransfersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuspiciousAccountTransfersServiceImpl implements SuspiciousAccountTransfersService {

    private static final Logger log = LoggerFactory.getLogger(SuspiciousAccountTransfersServiceImpl.class);

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
        log.info("Создание новой записи о подозрительном переводе: {}", transferDTO);

        // Проверка обязательных полей
        if (transferDTO.isSuspicious() && (transferDTO.getSuspiciousReason() == null || transferDTO.getSuspiciousReason().isEmpty())) {
            log.error("Причина подозрительности должна быть указана, если перевод помечен как подозрительный.");
            throw new IllegalArgumentException("Причина подозрительности должна быть указана, если перевод помечен как подозрительный.");
        }

        if (transferDTO.isBlocked() && (transferDTO.getBlockedReason() == null || transferDTO.getBlockedReason().isEmpty())) {
            log.error("Причина блокировки должна быть указана, если перевод помечен как заблокированный.");
            throw new IllegalArgumentException("Причина блокировки должна быть указана, если перевод помечен как заблокированный.");
        }

        // Преобразование DTO в сущность
        SuspiciousAccountTransfers entity = mapper.toEntity(transferDTO);
        log.info("Преобразование DTO в сущность завершено: {}", entity);

        // Сохранение в базу данных
        entity = repository.save(entity);
        log.info("Сущность сохранена в базе данных с ID: {}", entity.getId());

        // Возврат сохранённого объекта в виде DTO
        return mapper.toDTO(entity);
    }

    @Override
    public SuspiciousAccountTransfersDTO update(Long id, SuspiciousAccountTransfersDTO transferDTO) {
        log.info("Обновление записи о подозрительном переводе с ID: {}", id);

        SuspiciousAccountTransfers existing = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Запись с ID {} не найдена.", id);
                    return new IllegalArgumentException("Запись с ID " + id + " не найдена.");
                });

        existing.setBlocked(transferDTO.isBlocked());
        existing.setSuspicious(transferDTO.isSuspicious());
        existing.setBlockedReason(transferDTO.getBlockedReason());
        existing.setSuspiciousReason(transferDTO.getSuspiciousReason());
        log.info("Данные обновлены: {}", existing);

        SuspiciousAccountTransfers updatedEntity = repository.save(existing);
        log.info("Обновленная сущность сохранена в базе данных с ID: {}", updatedEntity.getId());

        return mapper.toDTO(updatedEntity);
    }

    @Override
    public void delete(Long id) {
        log.info("Удаление записи о подозрительном переводе с ID: {}", id);

        if (!repository.existsById(id)) {
            log.error("Запись с ID {} не найдена. Удаление невозможно.", id);
            throw new IllegalArgumentException("Запись с ID " + id + " не найдена.");
        }

        repository.deleteById(id);
        log.info("Запись с ID {} успешно удалена.", id);
    }

    @Override
    public SuspiciousAccountTransfersDTO findById(Long id) {
        log.info("Поиск записи о подозрительном переводе с ID: {}", id);

        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> {
                    log.error("Запись с ID {} не найдена.", id);
                    return new IllegalArgumentException("Запись с ID " + id + " не найдена.");
                });
    }

    @Override
    public List<SuspiciousAccountTransfersDTO> findAll() {
        log.info("Получение всех записей о подозрительных переводах.");

        List<SuspiciousAccountTransfersDTO> transfers = repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        log.info("Найдено {} записей о подозрительных переводах.", transfers.size());

        return transfers;
    }
}
