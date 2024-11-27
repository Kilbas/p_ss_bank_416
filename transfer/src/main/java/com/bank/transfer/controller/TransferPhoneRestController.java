package com.bank.transfer.controller;

import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.service.TransferPhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransferPhoneRestController {
    private final TransferPhoneService transferPhoneService;


}
