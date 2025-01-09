package com.bank.history.operations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OperationTest {

    @Test
    @DisplayName("Проверка массива Operation")
    void testOperationArray() {
        Operation[] operations = Operation.values();
        assertAll(
                () -> assertNotNull(operations),
                () -> assertEquals(3, operations.length),
                () -> assertEquals(Operation.CREATE, operations[0]),
                () -> assertEquals(Operation.UPDATE, operations[1]),
                () -> assertEquals(Operation.DELETE, operations[2])
        );
    }

    @Test
    @DisplayName("Проверка valueOf")
    void testOperationValue() {
        assertAll(
                () -> assertEquals(Operation.CREATE, Operation.valueOf("CREATE")),
                () -> assertEquals(Operation.UPDATE, Operation.valueOf("UPDATE")),
                () -> assertEquals(Operation.DELETE, Operation.valueOf("DELETE"))
        );
    }

    @Test
    @DisplayName("Проверка неверного valueOf")
    void testOperationInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> Operation.valueOf("INVALID"));
    }
}
