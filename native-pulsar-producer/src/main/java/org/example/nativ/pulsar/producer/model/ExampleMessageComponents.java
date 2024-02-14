package org.example.nativ.pulsar.producer.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

record Status(String id, String name, String resourceType) {
    Status() {
        this(UUID.randomUUID().toString(), "string", "View");
    }
}

record Type(String id, String name, String resourceType) {
    Type() {
        this(UUID.randomUUID().toString(), "string", "Table");
    }
}

public record ExampleMessageComponents(double articulationScore,
                                       int avgRating,
                                       String createdBy,
                                       long createdOn,
                                       String displayName,
                                       Table table,
                                       boolean excludedFromAutoHyperlinking,
                                       String id,
                                       String lastModifiedBy,
                                       long lastModifiedOn,
                                       String name,
                                       int ratingsCount,
                                       String resourceType,
                                       Status status,
                                       boolean system,
                                       Type type) {

    public ExampleMessageComponents() {
        this(89.5, 0, UUID.randomUUID().toString(), Instant.now().getEpochSecond(), "string", new Table(), false,
                UUID.randomUUID().toString(), "lastModifiedBy", System.currentTimeMillis(), "string", 0, "View", new Status(),
                true, new Type());
    }
}

record ConnectionStringParameter(String id,
                                 String label,
                                 String parameter,
                                 boolean required) {
    public ConnectionStringParameter() {
        this(UUID.randomUUID().toString(), "string", "string", true);
    }
}

record Table(String id,
             String name,
             String resourceType) {
    Table() {
        this(UUID.randomUUID().toString(), "string", "View");
    }
}

record JdbcDriver(
        String connectionString,
        ArrayList<ConnectionStringParameter> connectionStringParameters,
        String createdBy,
        long createdOn,
        String databaseName,
        String databaseVersion,
        String driver,
        String id,
        ArrayList<JdbcDriverFile> jdbcDriverFiles,
        String lastModifiedBy,
        long lastModifiedOn,
        String resourceType,
        boolean system) {
    JdbcDriver() {
        this("string", new ArrayList<ConnectionStringParameter>(), UUID.randomUUID().toString(), Instant.now().getEpochSecond(),
                "string", "string", "string", UUID.randomUUID().toString(), new ArrayList<JdbcDriverFile>(), "lastModifiedBy", System.currentTimeMillis(), "View", true);
    }
}

record JdbcDriverFile(String createdBy, long createdOn, String fileName, String fileType, String id,
                      String lastModifiedBy,
                      long lastModifiedOn,
                      String resourceType,
                      boolean system) {

    JdbcDriverFile() {

        this(UUID.randomUUID().toString(), Instant.now().getEpochSecond(),"string","string", UUID.randomUUID().toString(),"lastModifiedBy", System.currentTimeMillis(),"View",true);
    }

}

record JdbcProperties(Map<String, Object> properties) {
    public JdbcProperties() {
        this(Map.of("string", "string"));
    }
}



