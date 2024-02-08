package org.example.service;

import org.apache.pulsar.client.api.PulsarClientException;
import org.example.model.ExampleMessage;
import org.apache.pulsar.client.api.MessageId;

import java.util.concurrent.ExecutionException;

public interface ProducerService {

    MessageId send(ExampleMessage message) throws PulsarClientException, ExecutionException, InterruptedException;

}
