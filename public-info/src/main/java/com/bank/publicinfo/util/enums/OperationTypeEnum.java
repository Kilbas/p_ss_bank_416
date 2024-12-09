package com.bank.publicinfo.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationTypeEnum {

    CREATE("Создание"),
    UPDATE("Обновление"),
    DELETE("Удаление"),
    UNKNOWN("Неизвестная");

    private final String value;
}
