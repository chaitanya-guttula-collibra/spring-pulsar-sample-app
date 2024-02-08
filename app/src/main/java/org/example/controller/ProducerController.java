package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.example.model.ExampleMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/send")
@Slf4j
public class ProducerController {

    private final PulsarTemplate<String> pulsarTemplate;

    private final PulsarTemplate<ExampleMessage> pulsarTemplate1;

    private final Producer<ExampleMessage> producer;

    public ProducerController(PulsarTemplate<String> pulsarTemplate, PulsarTemplate<ExampleMessage> pulsarTemplate1, Producer<ExampleMessage> producer) {
        this.pulsarTemplate = pulsarTemplate;
        this.pulsarTemplate1 = pulsarTemplate1;
        this.producer = producer;
    }

    @Value("${org.example.topic}")
    public String topic;

    @Value("${org.example.topic1}")
    public String topic1;

    @PostMapping("/exampleMessage")
    public ResponseEntity<String> sendMessageJson() {
        try {
            String messageId = pulsarTemplate.newMessage("This is a test message with UUID : "
                    + UUID.randomUUID()).withTopic(topic).sendAsync().get().toString();
            log.info("Sent Message ID: {}", messageId);
            return ResponseEntity.ok(messageId);
        } catch (PulsarClientException | ExecutionException | InterruptedException e) {
            log.error("Error sending message", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/exampleMessageWithSchema")
    public ResponseEntity<String> sendMessageJsonWithSchema() {
        try {
            String messageId = pulsarTemplate1.newMessage(
                    new ExampleMessage()).withTopic(topic1).sendAsync().get().toString();
            log.info("Sent Message ID: {}", messageId);
            return ResponseEntity.ok(messageId);
        } catch (PulsarClientException | ExecutionException | InterruptedException e) {
            log.error("Error sending message", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/example")
    public ResponseEntity<String> sendMessageJsonWithNativeProducer() {
        try {
            String messageId = producer.newMessage(Schema.JSON(ExampleMessage.class)).sendAsync().get().toString();
            log.info("Sent Message ID: {}", messageId);
            return ResponseEntity.ok(messageId);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error sending message", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


}
