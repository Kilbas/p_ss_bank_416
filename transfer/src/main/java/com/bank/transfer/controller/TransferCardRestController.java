package com.bank.transfer.controller;

import com.bank.transfer.model.CardTransfer;
import com.bank.transfer.service.TransferCardService;
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
@RequestMapping("/card")
public class TransferCardRestController {
    private final TransferCardService transferCardService;

    @GetMapping
    public ResponseEntity<List<CardTransfer>> getCardTransfers() {
        return ResponseEntity.ok(transferCardService.getAllCardTransfers());
    }

    @GetMapping("{id}")
    public ResponseEntity<CardTransfer> getCardTransfer(@PathVariable Long id) {
        CardTransfer cardTransfer = transferCardService.getCardTransferById(id);
        return ResponseEntity.ok(cardTransfer);
    }

    @PostMapping
    public ResponseEntity<CardTransfer> createCardTransfer(@RequestBody CardTransfer cardTransfer) {
        transferCardService.addCardTransfer(cardTransfer);
        return ResponseEntity.ok(cardTransfer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardTransfer> updateCardTransfer(@RequestBody CardTransfer cardTransfer, @PathVariable long id) {
        transferCardService.updateCardTransfer(cardTransfer, id);
        return ResponseEntity.ok(cardTransfer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CardTransfer> deleteCardTransfer(@PathVariable long id) {
        transferCardService.deleteCardTransfer(id);
        return ResponseEntity.noContent().build();
    }
}
