package com.bank.antifraud.controller;

import com.bank.antifraud.dto.AuditDTO;
import com.bank.antifraud.service.AuditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/audits")
public class AuditController {

private final AuditService auditService;

public AuditController(AuditService auditService) {
    this.auditService = auditService;
}

    @GetMapping("/{id}")
    public ResponseEntity<AuditDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(auditService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AuditDTO>> getAll() {
        return ResponseEntity.ok(auditService.findAll());
    }

    @PostMapping
    public ResponseEntity<AuditDTO> create(@RequestBody AuditDTO auditDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auditService.create(auditDTO));
    }

    @GetMapping("/entity-type/{entityType}")
    public ResponseEntity<List<AuditDTO>> findByEntityType(@PathVariable String entityType) {
        return ResponseEntity.ok(auditService.findByEntityType(entityType));
    }

    @GetMapping("/operation-type/{operationType}")
    public ResponseEntity<List<AuditDTO>> findByOperationType(@PathVariable String operationType) {
        return ResponseEntity.ok(auditService.findByOperationType(operationType));
    }

}
