package com.bank.history.config;

import org.hibernate.dialect.PostgreSQL9Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class PostgresCustom extends PostgreSQL9Dialect {
    public PostgresCustom() {
        super();
        this.registerFunction("jsonExtract",
                new SQLFunctionTemplate(StandardBasicTypes.STRING, "CAST(?1 AS JSONB) ->> ?2"));
    }
}
