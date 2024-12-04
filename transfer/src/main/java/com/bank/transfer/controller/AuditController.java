package com.bank.transfer.controller;

import com.bank.transfer.dto.AuditDTO;
import com.bank.transfer.mapper.AuditMapper;
import com.bank.transfer.model.Audit;
import com.bank.transfer.service.AuditService;
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

    @GetMapping
    public ResponseEntity<List<AuditDTO>> getAllAudits() {
        List<Audit> audits = auditService.getAllAudit();
        List<AuditDTO> auditDTOs = audits.stream()
                .map(auditMapper::auditToAuditDTO)
                .toList();

        return ResponseEntity.ok(auditDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditDTO> getAuditById(@PathVariable long id) {
        Audit audit = auditService.getAuditById(id);
        AuditDTO auditDTO = auditMapper.auditToAuditDTO(audit);

        return ResponseEntity.ok(auditDTO);
    }
}
