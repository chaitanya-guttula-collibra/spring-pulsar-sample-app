package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.PulsarClientException;
import org.example.model.ExampleMessage;
import org.example.service.ProducerService;
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

    private final ProducerService producerService;

    public ProducerController(PulsarTemplate<String> pulsarTemplate, ProducerService producerService) {
        this.pulsarTemplate = pulsarTemplate;
        this.producerService = producerService;
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
            String messageId = producerService.send(new ExampleMessage()).toString();
            log.info("Sent Message ID: {}", messageId);
            return ResponseEntity.ok(messageId);
        } catch (PulsarClientException | ExecutionException | InterruptedException e) {
            log.error("Error sending message", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }



}
