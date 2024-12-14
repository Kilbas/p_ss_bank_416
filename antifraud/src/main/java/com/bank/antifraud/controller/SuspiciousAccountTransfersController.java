package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.service.SuspiciousAccountTransfersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Tag(name = "Подозрительные переводы по счетам", description = "Операции, связанные с подозрительными переводами по счетам")
@RestController
@RequestMapping("/api/v1/suspicious-account-transfers")
public class SuspiciousAccountTransfersController {

    private final SuspiciousAccountTransfersService service;

    public SuspiciousAccountTransfersController(SuspiciousAccountTransfersService service) {
        this.service = service;
    }

    @Operation(summary = "Получить перевод по ID", description = "Возвращает подозрительный перевод по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное выполнение"),
            @ApiResponse(responseCode = "404", description = "Перевод не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SuspiciousAccountTransfersDTO> findById(@PathVariable Long id) {
        SuspiciousAccountTransfersDTO dto = service.findByIdAccountTransfer(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Получить все переводы", description = "Возвращает список всех подозрительных переводов")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @GetMapping
    public ResponseEntity<List<SuspiciousAccountTransfersDTO>> findAll() {
        List<SuspiciousAccountTransfersDTO> transfers = service.findAllAccountTransfers();
        return ResponseEntity.ok(transfers);
    }

    @Operation(summary = "Создать перевод", description = "Создает новый подозрительный перевод")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Перевод успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "422", description = "Ошибка целостности данных")
    })
    @PostMapping
    public ResponseEntity<SuspiciousAccountTransfersDTO> create(
            @Valid @RequestBody SuspiciousAccountTransfersDTO dto) {
        SuspiciousAccountTransfersDTO createdDto = service.createNewAccountTransfer(dto);
        URI location = URI.create("/api/v1/suspicious-account-transfers/" + createdDto.getId());
        return ResponseEntity.created(location).body(createdDto);
    }

    @Operation(summary = "Обновить перевод", description = "Обновляет существующий подозрительный перевод по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Перевод успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Перевод не найден"),
            @ApiResponse(responseCode = "422", description = "Ошибка целостности данных")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SuspiciousAccountTransfersDTO> update(
            @PathVariable Long id, @Valid @RequestBody SuspiciousAccountTransfersDTO dto) {
        SuspiciousAccountTransfersDTO updatedDto = service.updateAccountTransfer(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    @Operation(summary = "Удалить перевод", description = "Удаляет подозрительный перевод по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Перевод успешно удален"),
            @ApiResponse(responseCode = "404", description = "Перевод не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteAccountTransfer(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Найти переводы по причине", description = "Возвращает список подозрительных переводов, содержащих указанную причину")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное выполнение"),
            @ApiResponse(responseCode = "204", description = "Нет данных")
    })
    @GetMapping("/reason")
    public ResponseEntity<List<SuspiciousAccountTransfersDTO>> findByReason(@RequestParam String reason) {
        List<SuspiciousAccountTransfersDTO> transfers = service.findTransfersByReason(reason);
        return transfers.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(transfers);
    }

    @Operation(summary = "Получить все заблокированные переводы", description = "Возвращает список всех заблокированных переводов")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @GetMapping("/blocked")
    public ResponseEntity<List<SuspiciousAccountTransfersDTO>> findBlockedTransfers() {
        return ResponseEntity.ok(service.findBlockedTransfers());
    }

    @Operation(summary = "Получить все подозрительные переводы", description = "Возвращает список всех переводов, отмеченных как подозрительные")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @GetMapping("/suspicious")
    public ResponseEntity<List<SuspiciousAccountTransfersDTO>> findSuspiciousTransfers() {
        return ResponseEntity.ok(service.findSuspiciousTransfers());
    }
}
