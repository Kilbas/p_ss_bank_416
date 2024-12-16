package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;
import com.bank.antifraud.service.SuspiciousPhoneTransfersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Получить перевод по ID", description = "Возвращает подозрительный телефонный перевод по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Перевод успешно получен"),
            @ApiResponse(responseCode = "404", description = "Перевод с указанным ID не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SuspiciousPhoneTransferDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Получить все переводы", description = "Возвращает список всех подозрительных телефонных переводов")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список переводов успешно возвращен"),
            @ApiResponse(responseCode = "204", description = "Переводов не найдено"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<SuspiciousPhoneTransferDTO>> findAll() {
        List<SuspiciousPhoneTransferDTO> transfers = service.findAll();
        return transfers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transfers);
    }

    @Operation(summary = "Создать новый перевод", description = "Создает новый подозрительный телефонный перевод")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Перевод успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные в запросе"),
            @ApiResponse(responseCode = "422", description = "Ошибка целостности данных")
    })
    @PostMapping
    public ResponseEntity<SuspiciousPhoneTransferDTO> create(@Valid @RequestBody SuspiciousPhoneTransferDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Обновить перевод", description = "Обновляет существующий перевод по указанному ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Перевод успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные в запросе"),
            @ApiResponse(responseCode = "404", description = "Перевод с указанным ID не найден"),
            @ApiResponse(responseCode = "422", description = "Ошибка целостности данных")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SuspiciousPhoneTransferDTO> update(@PathVariable Long id, @Valid @RequestBody SuspiciousPhoneTransferDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Удалить перевод", description = "Удаляет подозрительный телефонный перевод по указанному ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Перевод успешно удален"),
            @ApiResponse(responseCode = "404", description = "Перевод с указанным ID не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Найти переводы по причине", description = "Возвращает список переводов, содержащих указанную причину")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Переводы успешно найдены"),
            @ApiResponse(responseCode = "204", description = "Переводов по указанной причине не найдено"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    @GetMapping("/reason/{reason}")
    public ResponseEntity<List<SuspiciousPhoneTransferDTO>> findByReason(@PathVariable String reason) {
        List<SuspiciousPhoneTransferDTO> transfers = service.findTransfersByReason(reason);
        return transfers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transfers);
    }

    @Operation(summary = "Получить заблокированные переводы", description = "Возвращает список заблокированных переводов")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список заблокированных переводов возвращен"),
            @ApiResponse(responseCode = "204", description = "Заблокированных переводов не найдено")
    })
    @GetMapping("/blocked")
    public ResponseEntity<List<SuspiciousPhoneTransferDTO>> findBlockedTransfers() {
        List<SuspiciousPhoneTransferDTO> transfers = service.findBlockedTransfers();
        return transfers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transfers);
    }

    @Operation(summary = "Получить подозрительные переводы", description = "Возвращает список подозрительных переводов")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список подозрительных переводов возвращен"),
            @ApiResponse(responseCode = "204", description = "Подозрительных переводов не найдено")
    })
    @GetMapping("/suspicious")
    public ResponseEntity<List<SuspiciousPhoneTransferDTO>> findSuspiciousTransfers() {
        List<SuspiciousPhoneTransferDTO> transfers = service.findSuspiciousTransfers();
        return transfers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transfers);
    }
}
