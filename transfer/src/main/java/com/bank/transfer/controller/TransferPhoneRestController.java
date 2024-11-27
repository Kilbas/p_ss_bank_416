package com.bank.transfer.controller;

import com.bank.transfer.model.PhoneTransfer;
import com.bank.transfer.service.TransferPhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/phone")
public class TransferPhoneRestController {
    private final TransferPhoneService transferPhoneService;

    @GetMapping
    public ResponseEntity<List<PhoneTransfer>> getPhoneTransfers() {
        return ResponseEntity.ok(transferPhoneService.getAllPhoneTransfers());
    }

    @GetMapping("{id}")
    public ResponseEntity<PhoneTransfer> getPhoneTransfer(@PathVariable Long id) {
        PhoneTransfer phoneTransfer = transferPhoneService.getPhoneTransferById(id);
        return ResponseEntity.ok(phoneTransfer);
    }

    @PostMapping
    public ResponseEntity<PhoneTransfer> createPhoneTransfer(@RequestBody PhoneTransfer phoneTransfer) {
        transferPhoneService.addPhoneTransfer(phoneTransfer);
        return ResponseEntity.ok(phoneTransfer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhoneTransfer> updatePhoneTransfer(@RequestBody PhoneTransfer phoneTransfer, @PathVariable long id) {
        transferPhoneService.updatePhoneTransfer(phoneTransfer, id);
        return ResponseEntity.ok(phoneTransfer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PhoneTransfer> deletePhoneTransfer(@PathVariable long id) {
        transferPhoneService.deletePhoneTransfer(id);
        return ResponseEntity.noContent().build();
    }
}
