package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;
import com.bank.antifraud.entity.SuspiciousPhoneTransfers;
import com.bank.antifraud.mapper.SuspiciousPhoneTransfersMapper;
import com.bank.antifraud.repository.SuspiciousPhoneTransfersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SuspiciousPhoneTransfersServiceImpl implements SuspiciousPhoneTransfersService {

    private final SuspiciousPhoneTransfersRepository repository;
    private final SuspiciousPhoneTransfersMapper mapper;

    public SuspiciousPhoneTransfersServiceImpl(SuspiciousPhoneTransfersRepository repository,
                                               SuspiciousPhoneTransfersMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Найти подозрительный перевод по ID.
     *
     * @param id идентификатор перевода
     * @return SuspiciousPhoneTransferDTO найденный перевод
     * @throws IllegalArgumentException если перевод с указанным ID не найден
     */
    @Override
    public SuspiciousPhoneTransferDTO findById(Long id) {
        log.info("Попытка найти подозрительный перевод с ID: {}", id);
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> {
                    log.error("Подозрительный перевод с ID {} не найден.", id);
                    return new IllegalArgumentException("Перевод с ID " + id + " не найден.");
                });
    }

    /**
     * Найти все подозрительные переводы.
     *
     * @return Список SuspiciousPhoneTransferDTO
     */
    @Override
    public List<SuspiciousPhoneTransferDTO> findAll() {
        log.info("Запрос на получение всех подозрительных переводов.");
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Создать новый подозрительный перевод.
     *
     * @param transferDTO данные перевода
     * @return SuspiciousPhoneTransferDTO созданный перевод
     * @throws IllegalArgumentException если phoneTransferId равен null
     */
    @Override
    public SuspiciousPhoneTransferDTO create(SuspiciousPhoneTransferDTO transferDTO) {
        log.info("Попытка создать новый подозрительный перевод.");
        if (transferDTO.getPhoneTransferId() == null) {
            log.error("Поле phoneTransferId не может быть null.");
            throw new IllegalArgumentException("Поле phoneTransferId не может быть null.");
        }
        SuspiciousPhoneTransfers entity = mapper.toEntity(transferDTO);
        entity = repository.save(entity);
        log.info("Подозрительный перевод успешно создан с ID: {}", entity.getId());
        return mapper.toDTO(entity);
    }

    /**
     * Обновить существующий подозрительный перевод.
     *
     * @param id          идентификатор перевода для обновления
     * @param transferDTO данные перевода
     * @return SuspiciousPhoneTransferDTO обновленный перевод
     * @throws IllegalArgumentException если перевод с указанным ID не найден
     */
    @Override
    public SuspiciousPhoneTransferDTO update(Long id, SuspiciousPhoneTransferDTO transferDTO) {
        log.info("Попытка обновить подозрительный перевод с ID: {}", id);
        SuspiciousPhoneTransfers existing = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Подозрительный перевод с ID {} не найден.", id);
                    return new IllegalArgumentException("Перевод с ID " + id + " не найден.");
                });
        mapper.toDtoUpdate(transferDTO, existing);
        SuspiciousPhoneTransfers updatedEntity = repository.save(existing);
        log.info("Подозрительный перевод успешно обновлен с ID: {}", updatedEntity.getId());
        return mapper.toDTO(updatedEntity);
    }

    /**
     * Удалить подозрительный перевод по ID.
     *
     * @param id идентификатор перевода для удаления
     * @throws EntityNotFoundException если перевод с указанным ID не найден
     */
    @Override
    public void delete(Long id) {
        log.info("Попытка удалить подозрительный перевод с ID: {}", id);
        if (!repository.existsById(id)) {
            log.error("Подозрительный перевод с ID {} не найден. Удаление невозможно.", id);
            throw new EntityNotFoundException("Перевод с ID " + id + " не найден.");
        }
        repository.deleteById(id);
        log.info("Подозрительный перевод с ID {} успешно удален.", id);
    }

    /**
     * Найти подозрительные переводы по причине.
     *
     * @param reason причина
     * @return Список SuspiciousPhoneTransferDTO
     */
    @Override
    public List<SuspiciousPhoneTransferDTO> findTransfersByReason(String reason) {
        log.info("Поиск подозрительных переводов по причине: {}", reason);
        return repository.findBySuspiciousReasonContainingIgnoreCase(reason).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
