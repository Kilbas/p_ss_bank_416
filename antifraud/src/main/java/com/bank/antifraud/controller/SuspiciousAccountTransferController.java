package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.service.SuspiciousAccountTransfersService;
import liquibase.pro.packaged.R;
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
@RequestMapping("/api/suspicious-account-transfers")
public class SuspiciousAccountTransferController {

    private final SuspiciousAccountTransfersService service;

    public SuspiciousAccountTransferController(SuspiciousAccountTransfersService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuspiciousAccountTransfersDTO> findById(@PathVariable ("id") long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<SuspiciousAccountTransfersDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<SuspiciousAccountTransfersDTO> create(@Valid @RequestBody SuspiciousAccountTransfersDTO suspiciousAccountTransfersDTO) {
        return ResponseEntity.ok(service.create(suspiciousAccountTransfersDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuspiciousAccountTransfersDTO> update (@Valid @PathVariable ("id") long id, @RequestBody SuspiciousAccountTransfersDTO suspiciousAccountTransfersDTO) {
        return ResponseEntity.ok(service.update(id, suspiciousAccountTransfersDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ("id") long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/suspicious")
//    public ResponseEntity<List<SuspiciousAccountTransfersDTO>> findSuspiciousTransfers() {
//        return ResponseEntity.ok(service.(findSuspiciousTransfers));
//    }


}
