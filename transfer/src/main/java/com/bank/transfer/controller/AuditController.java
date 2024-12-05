package com.bank.transfer.controller;

import com.bank.transfer.dto.AuditDTO;
import com.bank.transfer.mapper.AuditMapper;
import com.bank.transfer.model.Audit;
import com.bank.transfer.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/audit")
public class AuditController {
    private final AuditService auditService;
    private final AuditMapper auditMapper;

    @Operation(summary = "Получение всех записей аудита",
            description = "Возвращает список всех записей аудита в формате AuditDTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список успешно получен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<AuditDTO>> getAllAudits() {
        List<Audit> audits = auditService.getAllAudit();
        List<AuditDTO> auditDTOs = audits.stream()
                .map(auditMapper::auditToAuditDTO)
                .toList();

        return ResponseEntity.ok(auditDTOs);
    }

    @Operation(summary = "Получение записи аудита по ID",
            description = "Возвращает запись аудита по переданному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись успешно получена"),
            @ApiResponse(responseCode = "404", description = "Запись с указанным ID не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuditDTO> getAuditById(@PathVariable long id) {
        Audit audit = auditService.getAuditById(id);
        AuditDTO auditDTO = auditMapper.auditToAuditDTO(audit);

        return ResponseEntity.ok(auditDTO);
    }
}
