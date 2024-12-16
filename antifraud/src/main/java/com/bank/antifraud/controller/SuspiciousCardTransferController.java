package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.service.SuspiciousCardTransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Подозрительные переводы с картами", description = "Операции, связанные с подозрительными переводами по картам")
@RestController
@RequestMapping("/suspicious-card-transfer")
public class SuspiciousCardTransferController {

    private final SuspiciousCardTransferService service;

    public SuspiciousCardTransferController(SuspiciousCardTransferService service) {
        this.service = service;
    }

    @Operation(summary = "Получить перевод по ID", description = "Возвращает подозрительный перевод по указанному ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Перевод найден"),
            @ApiResponse(responseCode = "404", description = "Перевод не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SuspiciousCardTransferDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdCardTransfer(id));
    }

    @Operation(summary = "Получить все переводы", description = "Возвращает список всех подозрительных переводов")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список переводов возвращен"),
            @ApiResponse(responseCode = "204", description = "Нет данных"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<SuspiciousCardTransferDTO>> findAll() {
        return ResponseEntity.ok(service.findAllCardTransfers());
    }

    @Operation(summary = "Создать перевод", description = "Создает новый подозрительный перевод")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Перевод успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "422", description = "Ошибка целостности данных")
    })
    @PostMapping
    public ResponseEntity<SuspiciousCardTransferDTO> create(@Valid @RequestBody SuspiciousCardTransferDTO dto) {
        return ResponseEntity.ok(service.createNewCardTransfer(dto));
    }

    @Operation(summary = "Обновить перевод", description = "Обновляет существующий перевод по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Перевод успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Перевод не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "422", description = "Ошибка целостности данных")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SuspiciousCardTransferDTO> update(@PathVariable Long id, @Valid @RequestBody SuspiciousCardTransferDTO dto) {
        return ResponseEntity.ok(service.updateCardTransfer(id, dto));
    }

    @Operation(summary = "Удалить перевод", description = "Удаляет перевод по указанному ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Перевод успешно удален"),
            @ApiResponse(responseCode = "404", description = "Перевод не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCardTransfer(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Найти переводы по причине", description = "Возвращает список подозрительных переводов, содержащих указанную причину")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное выполнение"),
            @ApiResponse(responseCode = "204", description = "Нет данных")
    })
    @GetMapping("/reason")
    public ResponseEntity<List<SuspiciousCardTransferDTO>> findByReason(@RequestParam String reason) {
        List<SuspiciousCardTransferDTO> transfers = service.findTransfersByReason(reason);
        return transfers.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(transfers);
    }

    @Operation(summary = "Получить все заблокированные переводы", description = "Возвращает список всех заблокированных переводов")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @GetMapping("/blocked")
    public ResponseEntity<List<SuspiciousCardTransferDTO>> findBlockedTransfers() {
        return ResponseEntity.ok(service.findBlockedTransfers());
    }

    @Operation(summary = "Получить все подозрительные переводы", description = "Возвращает список всех переводов, отмеченных как подозрительные")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @GetMapping("/suspicious")
    public ResponseEntity<List<SuspiciousCardTransferDTO>> findSuspiciousTransfers() {
        return ResponseEntity.ok(service.findSuspiciousTransfers());
    }
}
