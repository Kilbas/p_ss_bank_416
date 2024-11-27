package com.bank.transfer.controller;

import com.bank.transfer.service.TransferAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransferAccountRestController {
    private final TransferAccountService transferAccountService;
}
