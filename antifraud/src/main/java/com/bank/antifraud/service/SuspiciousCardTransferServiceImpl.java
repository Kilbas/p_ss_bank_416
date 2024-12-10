package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.entity.SuspiciousCardTransfer;
import com.bank.antifraud.mapper.SuspiciousCardTransferMapper;
import com.bank.antifraud.repository.SuspiciousCardTransferRepository;
import liquibase.pro.packaged.S;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с подозрительными переводами по картам.
 */
@Slf4j
@Service
public class SuspiciousCardTransferServiceImpl implements SuspiciousCardTransferService {


    private final SuspiciousCardTransferRepository repository;
    private final SuspiciousCardTransferMapper mapper;

    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param repository Репозиторий для работы с данными.
     * @param mapper     Маппер для преобразования между DTO и сущностью.
     */
    public SuspiciousCardTransferServiceImpl(SuspiciousCardTransferRepository repository,
                                             SuspiciousCardTransferMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Найти перевод по идентификатору.
     *
     * @param id Идентификатор перевода.
     * @return DTO подозрительного перевода.
     */
    @Override
    public SuspiciousCardTransferDTO findById(Long id) {
        log.info("Запрос на поиск подозрительного перевода по ID: {}", id);
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> {
                    log.error("Перевод с ID {} не найден", id);
                    return new EntityNotFoundException("Перевод не найден с ID: " + id);
                });
    }

    /**
     * Получить список всех переводов.
     *
     * @return Список DTO подозрительных переводов.
     */
    @Override
    public List<SuspiciousCardTransferDTO> findAll() {
        log.info("Запрос на получение всех подозрительных переводов");
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Создать новый подозрительный перевод.
     *
     * @param transferDTO DTO с данными для создания перевода.
     * @return DTO созданного перевода.
     */
    @Override
    public SuspiciousCardTransferDTO create(SuspiciousCardTransferDTO transferDTO) {
        log.info("Запрос на создание нового подозрительного перевода: {}", transferDTO);
        SuspiciousCardTransfer entity = mapper.toEntity(transferDTO);
        entity = repository.save(entity);
        log.info("Подозрительный перевод создан с ID: {}", entity.getId());
        return mapper.toDTO(entity);
    }

    /**
     * Обновить существующий подозрительный перевод.
     *
     * @param id          Идентификатор перевода для обновления.
     * @param transferDTO DTO с новыми данными.
     * @return Обновленный DTO перевода.
     */
    @Override
    public SuspiciousCardTransferDTO update(Long id, SuspiciousCardTransferDTO transferDTO) {

        SuspiciousCardTransfer existing = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Перевод с ID {} не найден для обновления", id);
                    return new IllegalArgumentException("Перевод не найден с ID: " + id);
                });
        mapper.toDtoUpdate(transferDTO, existing);

        SuspiciousCardTransfer updatedEntity = repository.save(existing);

        return mapper.toDTO(updatedEntity);
    }

    /**
     * Удалить подозрительный перевод по идентификатору.
     *
     * @param id Идентификатор перевода.
     */
    @Override
    public void delete(Long id) {
        log.info("Запрос на удаление подозрительного перевода с ID: {}", id);
        if (!repository.existsById(id)) {
            log.error("Перевод с ID {} не найден для удаления", id);
            throw new EntityNotFoundException("Перевод не найден с ID: " + id);
        }
        repository.deleteById(id);
        log.info("Подозрительный перевод с ID {} успешно удален", id);
    }

    /**
     * Найти все заблокированные переводы.
     *
     * @return Список DTO заблокированных переводов.
     */
    @Override
    public List<SuspiciousCardTransferDTO> findBlockedTransfers() {
        log.info("Запрос на получение всех заблокированных переводов");
        return repository.findAll().stream()
                .filter(SuspiciousCardTransfer::getIsBlocked)
                .map(mapper::toDTO)
                .toList();
    }
}
