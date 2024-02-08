package org.example.service.impl;

import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClientException;
import org.example.model.ExampleMessage;
import org.example.service.ProducerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class ProducerServiceImpl implements ProducerService {

    private final PulsarTemplate<ExampleMessage> producer;
    private final String topicName;

    public ProducerServiceImpl(PulsarTemplate<ExampleMessage> producer, @Qualifier("topicName") String topicName){
        this.producer = producer;
        this.topicName = topicName;
    }

    public MessageId send(ExampleMessage message) throws PulsarClientException, ExecutionException, InterruptedException {
            return producer.newMessage(message).withTopic(topicName).sendAsync().get();
    }

}
