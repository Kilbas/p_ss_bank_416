package com.bank.transfer;

import com.bank.transfer.model.AccountTransfer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class TransferApplication {
    public static void main(String[] args) {

        SpringApplication.run(TransferApplication.class, args);

    }
}
