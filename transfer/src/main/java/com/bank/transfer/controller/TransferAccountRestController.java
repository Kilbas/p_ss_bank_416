package com.bank.transfer.controller;

import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.service.TransferAccountService;
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
@RequestMapping("/account")
public class TransferAccountRestController {
    private final TransferAccountService transferAccountService;

    @GetMapping
    public ResponseEntity<List<AccountTransfer>> getAccountTransfers() {
        return ResponseEntity.ok(transferAccountService.getAllAccountTransfers());
    }

    @GetMapping("{id}")
    public ResponseEntity<AccountTransfer> getAccountTransfer(@PathVariable Long id) {
        AccountTransfer accountTransfer = transferAccountService.getAccountTransferById(id);
        return ResponseEntity.ok(accountTransfer);
    }

    @PostMapping
    public ResponseEntity<AccountTransfer> createAccountTransfer(@RequestBody AccountTransfer accountTransfer) {
        transferAccountService.addAccountTransfer(accountTransfer);
        return ResponseEntity.ok(accountTransfer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountTransfer> updateAccountTransfer(@RequestBody AccountTransfer accountTransfer, @PathVariable long id) {
        transferAccountService.updateAccountTransfer(accountTransfer, id);
        return ResponseEntity.ok(accountTransfer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AccountTransfer> deleteAccountTransfer(@PathVariable long id) {
        transferAccountService.deleteAccountTransfer(id);
        return ResponseEntity.noContent().build();
    }
}
