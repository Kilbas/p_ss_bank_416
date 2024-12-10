package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.service.SuspiciousCardTransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/suspicious-card-transfer")
public class SuspiciousCardTransferController {

    private final SuspiciousCardTransferService service;

    public SuspiciousCardTransferController(SuspiciousCardTransferService suspiciousCardTransferService) {
        this.service = suspiciousCardTransferService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuspiciousCardTransferDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<SuspiciousCardTransferDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<SuspiciousCardTransferDTO> create(
            @Valid @RequestBody SuspiciousCardTransferDTO suspiciousCardTransferDTO) {
        return ResponseEntity.ok(service.create(suspiciousCardTransferDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuspiciousCardTransferDTO> update(
            @PathVariable Long id, @Valid @RequestBody SuspiciousCardTransferDTO suspiciousCardTransferDTO) {
        return ResponseEntity.ok(service.update(id, suspiciousCardTransferDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/blocked")
    public ResponseEntity<List<SuspiciousCardTransferDTO>> findBlockedTransfers() {
        return ResponseEntity.ok(service.findBlockedTransfers());
    }


}
