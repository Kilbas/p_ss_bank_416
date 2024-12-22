package com.bank.publicinfo.util;

import com.bank.publicinfo.entity.Audit;
import org.apache.commons.lang.RandomStringUtils;

import java.time.LocalDateTime;
import java.util.Random;

public class CreateAudit {

    public static Audit createAuditAdd() {
        Audit audit = new Audit();
        audit.setId(createRandomLong());
        audit.setEntityType(createRandomString());
        audit.setOperationType(createRandomString());
        audit.setCreatedBy(createRandomString());
        audit.setCreatedAt(LocalDateTime.now());
        return audit;
    }

    private static Long createRandomLong() {
        Random random = new Random();
        return random.nextLong(1000L);
    }

    private static String createRandomString() {
        return RandomStringUtils.random(40, true, true);
    }
}