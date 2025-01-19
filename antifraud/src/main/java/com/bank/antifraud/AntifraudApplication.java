package com.bank.antifraud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AntifraudApplication {
    public static void main(String[] args) {

        SpringApplication.run(AntifraudApplication.class, args);

    }
}
//docker exec -it antifraud-bank-db-1 psql -U postgres
//CREATE SCHEMA anti_fraud;
//\dn
//\q
