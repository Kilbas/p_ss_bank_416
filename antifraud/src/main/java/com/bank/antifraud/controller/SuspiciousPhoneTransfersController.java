package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;
import com.bank.antifraud.service.SuspiciousPhoneTransfersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Подозрительные телефонные переводы", description = "Операции, связанные с подозрительными переводами по телефону")
@RestController
@RequestMapping("/suspicious-phone-transfers")
public class SuspiciousPhoneTransfersController {

    private final SuspiciousPhoneTransfersService service;

    public SuspiciousPhoneTransfersController(SuspiciousPhoneTransfersService service) {
        this.service = service;
    }

    @Operation(
            summary = "Получить перевод по ID",
            description = "Возвращает подозрительный телефонный перевод по указанному идентификатору"
    )
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "404", description = "Перевод не найден")
    @GetMapping("/{id}")
    public ResponseEntity<SuspiciousPhoneTransferDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Получить все переводы",
            description = "Возвращает список всех подозрительных телефонных переводов"
    )
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "204", description = "Нет данных")
    @GetMapping
    public ResponseEntity<List<SuspiciousPhoneTransferDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Создать перевод",
            description = "Создает новый подозрительный телефонный перевод"
    )
    @ApiResponse(responseCode = "200", description = "Перевод успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @PostMapping
    public ResponseEntity<SuspiciousPhoneTransferDTO> create(
            @Valid @RequestBody SuspiciousPhoneTransferDTO suspiciousPhoneTransferDTO) {
        return ResponseEntity.ok(service.create(suspiciousPhoneTransferDTO));
    }

    @Operation(
            summary = "Обновить перевод",
            description = "Обновляет существующий подозрительный телефонный перевод по ID"
    )
    @ApiResponse(responseCode = "200", description = "Перевод успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Перевод не найден")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @PutMapping("/{id}")
    public ResponseEntity<SuspiciousPhoneTransferDTO> update(
            @PathVariable Long id, @Valid @RequestBody SuspiciousPhoneTransferDTO suspiciousPhoneTransferDTO) {
        return ResponseEntity.ok(service.update(id, suspiciousPhoneTransferDTO));
    }

    @Operation(
            summary = "Удалить перевод",
            description = "Удаляет подозрительный телефонный перевод по указанному идентификатору"
    )
    @ApiResponse(responseCode = "204", description = "Перевод успешно удален")
    @ApiResponse(responseCode = "404", description = "Перевод не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Найти переводы по причине",
            description = "Возвращает список подозрительных телефонных переводов, содержащих указанную причину"
    )
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "204", description = "Нет данных")
    @GetMapping("/reason/{reason}")
    public ResponseEntity<List<SuspiciousPhoneTransferDTO>> findByReason(@PathVariable String reason) {
        List<SuspiciousPhoneTransferDTO> transfers = service.findTransfersByReason(reason);
        if (transfers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transfers);
    }

    @Operation(
            summary = "Получить все заблокированные переводы",
            description = "Возвращает список всех заблокированных телефонных переводов"
    )
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "204", description = "Нет данных")
    @GetMapping("/blocked")
    public ResponseEntity<List<SuspiciousPhoneTransferDTO>> findBlockedTransfers() {
        return ResponseEntity.ok(service.findBlockedTransfers());
    }

    @Operation(
            summary = "Получить все подозрительные переводы",
            description = "Возвращает список всех телефонных переводов, отмеченных как подозрительные"
    )
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "204", description = "Нет данных")
    @GetMapping("/suspicious")
    public ResponseEntity<List<SuspiciousPhoneTransferDTO>> findSuspiciousPhoneTransfers() {
        return ResponseEntity.ok(service.findSuspiciousTransfers());
    }
}
