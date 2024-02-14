package org.example.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public record ExampleMessage(
        String createdBy,
        long createdOn,
        String cronExpression,
        String cronTimeZone,
        boolean detectAdvancedDataTypes,
        boolean executeProfiling,
        boolean extractDataSample,
        String id,
        JdbcDriver jdbcDriver,
        JdbcProperties jdbcProperties,
        String jobServer,
        String lastModifiedBy,
        long lastModifiedOn,
        String resourceType,
        List<ExampleMessageComponents> exampleMessageComponents,
        boolean system,
        ArrayList<String> tablesToSkip,
        String user){

    public ExampleMessage() {
        this(UUID.randomUUID().toString(), Instant.now().getEpochSecond(), "string", "string", false,
                false,
                false,
                UUID.randomUUID().toString(),
                new JdbcDriver(),
                new JdbcProperties(),
                "string",
                "lastModifiedBy",
                System.currentTimeMillis(),
                "View",
                Arrays.asList(new ExampleMessageComponents(), new ExampleMessageComponents()),
                true,
                new ArrayList<String>(),
                "string");
    }
}
