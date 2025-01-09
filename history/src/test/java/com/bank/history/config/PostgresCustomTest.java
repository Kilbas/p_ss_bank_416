package com.bank.history.config;

import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostgresCustomTest {

    @Test
    void testFunctionRegistration() {
        PostgresCustom postgresCustom = new PostgresCustom();
        SQLFunction function = postgresCustom.getFunctions().get("jsonextract");
        assertAll(
                () -> assertNotNull(function),
                () -> assertInstanceOf(SQLFunctionTemplate.class, function)
        );
    }
}
