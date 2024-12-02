package com.bank.transfer.controller;

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

    @GetMapping
    public ResponseEntity<List<Audit>> getAllAudits() {
        return ResponseEntity.ok(auditService.getAllAudit());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Audit> getAuditById(@PathVariable long id) {
        Audit audit = auditService.getAuditById(id);
        return ResponseEntity.ok(audit);
    }
}
