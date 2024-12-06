package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;
import com.bank.antifraud.service.SuspiciousPhoneTransfersService;
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
@RequestMapping("/api/suspicious-phone-transfers")
public class SuspiciousPhoneTransfersController {

    private final SuspiciousPhoneTransfersService service;

    public SuspiciousPhoneTransfersController(SuspiciousPhoneTransfersService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuspiciousPhoneTransferDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<SuspiciousPhoneTransferDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<SuspiciousPhoneTransferDTO> create(@Valid @RequestBody SuspiciousPhoneTransferDTO suspiciousPhoneTransferDTO) {
        return ResponseEntity.ok(service.create(suspiciousPhoneTransferDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuspiciousPhoneTransferDTO> update(@Valid @PathVariable Long id, @RequestBody SuspiciousPhoneTransferDTO suspiciousPhoneTransferDTO) {
        return ResponseEntity.ok(service.update(id, suspiciousPhoneTransferDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuspiciousPhoneTransferDTO> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reason/{reason}")
    public ResponseEntity<List<SuspiciousPhoneTransferDTO>> findByReason(@PathVariable String reason) {
        return ResponseEntity.ok(service.findTransfersByReason(reason));
    }

}
