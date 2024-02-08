package org.example.nativ.pulsar.producer.model;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public record ExampleMessage(
        UUID id, UUID createdBy, Instant createdOn, UUID lastModifiedBy, OffsetDateTime lastModifiedOn) {
    public ExampleMessage(UUID id, UUID createdBy, Instant createdOn) {
        this(id, createdBy, createdOn, randomUUID(), OffsetDateTime.now());
    }

    public ExampleMessage() {
        this(randomUUID(), randomUUID(), Instant.now(), randomUUID(), OffsetDateTime.now());
    }
}
