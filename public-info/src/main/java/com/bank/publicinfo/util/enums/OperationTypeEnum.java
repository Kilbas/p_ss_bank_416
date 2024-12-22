package com.bank.publicinfo.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationTypeEnum {

    CREATE("Создание"),
    UPDATE("Обновление"),
    UNSUPPORTED_OPERATION("unsupportedOperation");

    private final String value;
}