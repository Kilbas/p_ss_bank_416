package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.service.SuspiciousCardTransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Подозрительные переводы с картами",
        description = "Операции, связанные с подозрительными операциями по картам")
@RestController
@RequestMapping("/suspicious-card-transfer")
public class SuspiciousCardTransferController {

    private final SuspiciousCardTransferService service;

    public SuspiciousCardTransferController(SuspiciousCardTransferService suspiciousCardTransferService) {
        this.service = suspiciousCardTransferService;
    }

    @Operation(
            summary = "Получить перевод по ID",
            description = "Возвращает подозрительный перевод по указанному идентификатору"
    )
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "404", description = "Перевод не найден")
    @GetMapping("/{id}")
    public ResponseEntity<SuspiciousCardTransferDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdCardTransfer(id));
    }

    @Operation(
            summary = "Получить все переводы",
            description = "Возвращает список всех подозрительных переводов"
    )
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "204", description = "Нет данных")
    @GetMapping
    public ResponseEntity<List<SuspiciousCardTransferDTO>> findAll() {
        return ResponseEntity.ok(service.findAllCardTransfers());
    }

    @Operation(
            summary = "Создать перевод",
            description = "Создает новый подозрительный перевод"
    )
    @ApiResponse(responseCode = "200", description = "Перевод успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @PostMapping
    public ResponseEntity<SuspiciousCardTransferDTO> create(
            @Valid @RequestBody SuspiciousCardTransferDTO suspiciousCardTransferDTO) {
        return ResponseEntity.ok(service.createNewCardTransfer(suspiciousCardTransferDTO));
    }

    @Operation(
            summary = "Обновить перевод",
            description = "Обновляет существующий подозрительный перевод по ID"
    )
    @ApiResponse(responseCode = "200", description = "Перевод успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Перевод не найден")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @PutMapping("/{id}")
    public ResponseEntity<SuspiciousCardTransferDTO> update(
            @PathVariable Long id, @Valid @RequestBody SuspiciousCardTransferDTO suspiciousCardTransferDTO) {
        return ResponseEntity.ok(service.updateCardTransfer(id, suspiciousCardTransferDTO));
    }

    @Operation(
            summary = "Удалить перевод",
            description = "Удаляет подозрительный перевод по указанному идентификатору"
    )
    @ApiResponse(responseCode = "204", description = "Перевод успешно удален")
    @ApiResponse(responseCode = "404", description = "Перевод не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCardTransfer(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Найти переводы по причине",
            description = "Возвращает список подозрительных переводов, содержащих указанную причину"
    )
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "204", description = "Нет данных")
    @GetMapping("/reason/{reason}")
    public ResponseEntity<List<SuspiciousCardTransferDTO>> findByReason(@PathVariable String reason) {
        List<SuspiciousCardTransferDTO> transfers = service.findTransfersByReason(reason);
        if (transfers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transfers);
    }

    @Operation(
            summary = "Получить все заблокированные переводы",
            description = "Возвращает список всех заблокированных переводов"
    )
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "204", description = "Нет данных")
    @GetMapping("/blocked")
    public ResponseEntity<List<SuspiciousCardTransferDTO>> findBlockedTransfers() {
        return ResponseEntity.ok(service.findBlockedTransfers());
    }

    @Operation(
            summary = "Получить все подозрительные переводы",
            description = "Возвращает список всех переводов, отмеченных как подозрительные"
    )
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "204", description = "Нет данных")
    @GetMapping("/suspicious")
    public ResponseEntity<List<SuspiciousCardTransferDTO>> findSuspiciousCardTransfers() {
        return ResponseEntity.ok(service.findSuspiciousTransfers());
    }
}
