package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.service.SuspiciousCardTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для управления подозрительными переводами по картам.
 */
@RestController
@RequestMapping("/api/suspicious-card-transfer")
public class SuspiciousCardTransferController {

    private static final Logger log = LoggerFactory.getLogger(SuspiciousCardTransferController.class);

    private final SuspiciousCardTransferService service;

    /**
     * Конструктор для внедрения зависимости сервиса.
     *
     * @param suspiciousCardTransferService Сервис для обработки логики.
     */
    public SuspiciousCardTransferController(SuspiciousCardTransferService suspiciousCardTransferService) {
        this.service = suspiciousCardTransferService;
    }

    /**
     * Получение перевода по ID.
     *
     * @param id Идентификатор перевода.
     * @return DTO подозрительного перевода.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuspiciousCardTransferDTO> findById(@PathVariable ("id") Long id) {
        log.info("Получение подозрительного перевода по ID: {}", id);
        SuspiciousCardTransferDTO result = service.findById(id);
        log.info("Подозрительный перевод найден: {}", result);
        return ResponseEntity.ok(result);
    }

    /**
     * Получение всех подозрительных переводов.
     *
     * @return Список DTO подозрительных переводов.
     */
    @GetMapping
    public ResponseEntity<List<SuspiciousCardTransferDTO>> findAll() {
        log.info("Получение всех подозрительных переводов");
        List<SuspiciousCardTransferDTO> result = service.findAll();
        log.info("Найдено {} подозрительных переводов", result.size());
        return ResponseEntity.ok(result);
    }

    /**
     * Создание нового подозрительного перевода.
     *
     * @param suspiciousCardTransferDTO DTO с данными нового перевода.
     * @return DTO созданного перевода.
     */
    @PostMapping
    public ResponseEntity<SuspiciousCardTransferDTO> create(@Valid @RequestBody SuspiciousCardTransferDTO suspiciousCardTransferDTO) {
        log.info("Создание нового подозрительного перевода: {}", suspiciousCardTransferDTO);
        SuspiciousCardTransferDTO created = service.create(suspiciousCardTransferDTO);
        log.info("Подозрительный перевод успешно создан: {}", created);
        return ResponseEntity.ok(created);
    }

    /**
     * Обновление существующего подозрительного перевода.
     *
     * @param id                        Идентификатор перевода для обновления.
     * @param suspiciousCardTransferDTO DTO с новыми данными.
     * @return DTO обновленного перевода.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SuspiciousCardTransferDTO> update(@Valid @PathVariable ("id") Long id,
                                                            @RequestBody SuspiciousCardTransferDTO suspiciousCardTransferDTO) {
        log.info("Обновление подозрительного перевода с ID: {}, данными: {}", id, suspiciousCardTransferDTO);
        SuspiciousCardTransferDTO updated = service.update(id, suspiciousCardTransferDTO);
        log.info("Подозрительный перевод с ID {} успешно обновлен: {}", id, updated);
        return ResponseEntity.ok(updated);
    }

    /**
     * Удаление подозрительного перевода.
     *
     * @param id Идентификатор перевода для удаления.
     * @return HTTP статус 204 (No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ("id") Long id) {
        log.info("Удаление подозрительного перевода с ID: {}", id);
        service.delete(id);
        log.info("Подозрительный перевод с ID {} успешно удален", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получение всех заблокированных переводов.
     *
     * @return Список DTO заблокированных переводов.
     */
    @GetMapping("/blocked")
    public ResponseEntity<List<SuspiciousCardTransferDTO>> findBlockedTransfers() {
        log.info("Получение всех заблокированных переводов");
        List<SuspiciousCardTransferDTO> result = service.findBlockedTransfers();
        log.info("Найдено {} заблокированных переводов", result.size());
        return ResponseEntity.ok(result);
    }
}
