package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;
import com.bank.antifraud.service.SuspiciousPhoneTransfersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для управления подозрительными телефонными переводами.
 * Предоставляет API для создания, обновления, удаления и поиска переводов.
 */
@RestController
@RequestMapping("/api/suspicious-phone-transfers")
public class SuspiciousPhoneTransfersController {

    private static final Logger logger = LoggerFactory.getLogger(SuspiciousPhoneTransfersController.class);

    private final SuspiciousPhoneTransfersService service;

    public SuspiciousPhoneTransfersController(SuspiciousPhoneTransfersService service) {
        this.service = service;
    }

    /**
     * Получение подозрительного телефонного перевода по его ID.
     *
     * @param id Идентификатор перевода.
     * @return DTO перевода.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuspiciousPhoneTransferDTO> findById(@PathVariable("id") Long id) {
        logger.info("Получен запрос на получение подозрительного телефонного перевода с ID: {}", id);
        SuspiciousPhoneTransferDTO transfer = service.findById(id);
        logger.info("Возвращены данные перевода: {}", transfer);
        return ResponseEntity.ok(transfer);
    }

    /**
     * Получение списка всех подозрительных телефонных переводов.
     *
     * @return Список DTO переводов.
     */
    @GetMapping
    public ResponseEntity<List<SuspiciousPhoneTransferDTO>> findAll() {
        logger.info("Получен запрос на получение всех подозрительных телефонных переводов.");
        List<SuspiciousPhoneTransferDTO> transfers = service.findAll();
        logger.info("Возвращен список всех переводов, количество записей: {}", transfers.size());
        return ResponseEntity.ok(transfers);
    }

    /**
     * Создание нового подозрительного телефонного перевода.
     *
     * @param suspiciousPhoneTransferDTO Данные для создания перевода.
     * @return Созданный DTO перевода.
     */
    @PostMapping
    public ResponseEntity<SuspiciousPhoneTransferDTO> create(
            @Valid @RequestBody SuspiciousPhoneTransferDTO suspiciousPhoneTransferDTO) {
        logger.info("Получен запрос на создание подозрительного перевода: {}", suspiciousPhoneTransferDTO);
        SuspiciousPhoneTransferDTO createdTransfer = service.create(suspiciousPhoneTransferDTO);
        logger.info("Подозрительный перевод успешно создан: {}", createdTransfer);
        return ResponseEntity.ok(createdTransfer);
    }

    /**
     * Обновление данных о подозрительном телефонном переводе.
     *
     * @param id                         ID перевода.
     * @param suspiciousPhoneTransferDTO Новые данные перевода.
     * @return Обновленный DTO перевода.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SuspiciousPhoneTransferDTO> update(
            @Valid @PathVariable Long id, @RequestBody SuspiciousPhoneTransferDTO suspiciousPhoneTransferDTO) {
        logger.info("Получен запрос на обновление перевода с ID: {}. Новые данные: {}", id, suspiciousPhoneTransferDTO);
        SuspiciousPhoneTransferDTO updatedTransfer = service.update(id, suspiciousPhoneTransferDTO);
        logger.info("Подозрительный перевод успешно обновлен: {}", updatedTransfer);
        return ResponseEntity.ok(updatedTransfer);
    }

    /**
     * Удаление подозрительного телефонного перевода по его ID.
     *
     * @param id ID перевода.
     * @return HTTP 204 (No Content) при успешном удалении.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<SuspiciousPhoneTransferDTO> delete(@PathVariable Long id) {
        logger.info("Получен запрос на удаление подозрительного телефонного перевода с ID: {}", id);
        service.delete(id);
        logger.info("Подозрительный перевод с ID: {} успешно удален.", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Поиск подозрительных переводов по причине.
     *
     * @param reason Причина подозрительного перевода.
     * @return Список переводов, соответствующих причине.
     */
    @GetMapping("/reason/{reason}")
    public ResponseEntity<List<SuspiciousPhoneTransferDTO>> findByReason(@PathVariable String reason) {
        logger.info("Получен запрос на поиск подозрительных переводов по причине: {}", reason);
        List<SuspiciousPhoneTransferDTO> transfers = service.findTransfersByReason(reason);
        if (transfers.isEmpty()) {
            logger.info("Переводы с причиной '{}' не найдены.", reason);
            return ResponseEntity.noContent().build();
        }
        logger.info("Найдены переводы по причине '{}', количество записей: {}", reason, transfers.size());
        return ResponseEntity.ok(transfers);
    }
}
