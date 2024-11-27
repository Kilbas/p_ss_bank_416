package com.bank.transfer.controller;

import com.bank.transfer.service.TransferCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransferCardRestController {
    private final TransferCardService transferCardService;
}
