package com.bank.antifraud.service;

import com.bank.antifraud.dto.AuditDTO;
import com.bank.antifraud.entity.Audit;
import com.bank.antifraud.mapper.AuditMapper;
import com.bank.antifraud.repository.AuditRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuditServiceImpl implements AuditService {

    private static final Logger logger = LoggerFactory.getLogger(AuditServiceImpl.class);

    private final AuditRepository repository;
    private final AuditMapper mapper;

    public AuditServiceImpl(AuditRepository repository, AuditMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Метод для поиска аудита по ID.
     *
     * @param id идентификатор аудита
     * @return AuditDTO найденный аудит
     * @throws EntityNotFoundException если аудит с указанным ID не найден
     */
    @Override
    public AuditDTO findById(Long id) {

        if (id == null) {
            logger.error("Идентификатор ID не может быть null");
            throw new IllegalArgumentException("ID не может быть null");
        }

        AuditDTO auditDTO = repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> {
                    logger.error("Аудит с ID {} не найден", id);
                    return new EntityNotFoundException("Audit not found with ID: " + id);
                });
        return auditDTO;
    }

    /**
     * Метод для получения всех записей аудита.
     *
     * @return список всех AuditDTO
     */
    @Override
    public List<AuditDTO> findAll() {
        List<AuditDTO> audits = repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Найдено {} записей аудита", audits.size());
        return audits;
    }

    /**
     * Метод для создания новой записи аудита.
     *
     * @param transferDTO DTO для создания аудита
     * @return AuditDTO созданный аудит
     */
    @Override
    public AuditDTO create(AuditDTO transferDTO) {

        if (transferDTO == null) {
            logger.error("AuditDTO не может быть null");
            throw new IllegalArgumentException("AuditDTO не может быть null");
        }

        Audit entity = mapper.toEntity(transferDTO);
        Audit savedEntity = repository.save(entity);
        return mapper.toDTO(savedEntity);
    }

    /**
     * Метод для поиска аудита по типу сущности.
     *
     * @param entityType тип сущности
     * @return список AuditDTO с указанным типом сущности
     */
    @Override
    public List<AuditDTO> findByEntityType(String entityType) {

        if (entityType == null || entityType.isEmpty()) {
            logger.error("Тип сущности не может быть null или пустым");
            throw new IllegalArgumentException("Тип сущности не может быть null или пустым");
        }

        List<AuditDTO> audits = repository.findByEntityType(entityType).stream()
                .map(mapper::toDTO)
                .toList();
        return audits;
    }

    /**
     * Метод для поиска аудита по типу операции.
     *
     * @param operationType тип операции
     * @return список AuditDTO с указанным типом операции
     */
    @Override
    public List<AuditDTO> findByOperationType(String operationType) {

        if (operationType == null || operationType.isEmpty()) {
            logger.error("Тип операции не может быть null или пустым");
            throw new IllegalArgumentException("Тип операции не может быть null или пустым");
        }

        List<AuditDTO> audits = repository.findByOperationType(operationType).stream()
                .map(mapper::toDTO)
                .toList();
        return audits;
    }
}
