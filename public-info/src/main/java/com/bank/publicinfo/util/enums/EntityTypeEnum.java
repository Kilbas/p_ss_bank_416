package com.bank.publicinfo.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntityTypeEnum {

    BANK_DETAILS("BankDetails"),
    ATM("Atm"),
    BRANCH("Branch"),
    CERTIFICATE("Certificate"),
    LICENSE("License"),
    UNSUPPORTED_ENTITY("unsupportedEntity");

    private final String value;
}