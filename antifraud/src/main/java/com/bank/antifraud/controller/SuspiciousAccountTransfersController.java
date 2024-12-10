package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.service.SuspiciousAccountTransfersService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;



/**
 * Контроллер для управления подозрительными банковскими переводами
 */

@RestController
@RequestMapping("/api/suspicious-account-transfers")
public class SuspiciousAccountTransfersController {

private static final Logger logger = LoggerFactory.getLogger(SuspiciousAccountTransfersController.class);

    private final SuspiciousAccountTransfersService service;

    public SuspiciousAccountTransfersController(SuspiciousAccountTransfersService service) {
        this.service = service;
    }

    /**
     * Получение информации о подозрительном переводе по его ID
     * @param id ID перевода
     * @return DTO с данными перевода
     */

    @GetMapping("/{id}")
    public ResponseEntity<SuspiciousAccountTransfersDTO> findById(@PathVariable("id") long id) {
        logger.info("Получен запрос на получение подозрительного перевода по ID: {}", id);
        SuspiciousAccountTransfersDTO transfer = service.findById(id);

        return ResponseEntity.ok(transfer);

    }

    /**
     * Получение списка всех подозрительных операций
     * @return DTO с данными перевода
     */
    @GetMapping
    public ResponseEntity<List<SuspiciousAccountTransfersDTO>> findAll() {
        logger.info("Получен запрос на получение всех подозрительных переводов.");
        List<SuspiciousAccountTransfersDTO> transfers = service.findAll();
        logger.info("Возвращен список всех подозрительных переводов, количество: {}", transfers.size());
        return ResponseEntity.ok(transfers);
    }

    /**
     * Создание нового подозрительного перевода.
     *
     * @param suspiciousAccountTransfersDTO Данные для создания перевода.
     * @return DTO созданного перевода.
     */
    @PostMapping
    public ResponseEntity<SuspiciousAccountTransfersDTO> create(
            @Valid @RequestBody SuspiciousAccountTransfersDTO suspiciousAccountTransfersDTO) {
        logger.info("Создание нового запроса");
        SuspiciousAccountTransfersDTO createdTransfer = service.create(suspiciousAccountTransfersDTO);
        return ResponseEntity.ok(createdTransfer);
    }

    /**
     * Обновление данных о подозрительном переводе.
     *
     * @param id                         ID перевода.
     * @param suspiciousAccountTransfersDTO Новые данные перевода.
     * @return Обновленный DTO перевода.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SuspiciousAccountTransfersDTO> update(
            @Valid @PathVariable("id") long id, @RequestBody SuspiciousAccountTransfersDTO suspiciousAccountTransfersDTO) {
        logger.info("Получен запрос на обновление подозрительного перевода с ID: {}." +
                " Новые данные: {}", id, suspiciousAccountTransfersDTO);
        SuspiciousAccountTransfersDTO updatedTransfer = service.update(id, suspiciousAccountTransfersDTO);
        logger.info("Подозрительный перевод успешно обновлен: {}", updatedTransfer);
        return ResponseEntity.ok(updatedTransfer);
    }

    /**
     * Удаление подозрительного перевода по его ID.
     *
     * @param id ID перевода.
     * @return HTTP 204 (No Content) при успешном удалении.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        logger.info("Получен запрос на удаление подозрительного перевода с ID: {}", id);
        service.delete(id);
        logger.info("Подозрительный перевод с ID: {} успешно удален.", id);
        return ResponseEntity.noContent().build();
    }

}
