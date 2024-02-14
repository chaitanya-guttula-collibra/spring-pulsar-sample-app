package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.PulsarClientException;
import org.example.model.ExampleMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/send")
@Slf4j
public class ProducerController {

    private final PulsarTemplate<String> pulsarTemplate;

    private final PulsarTemplate<ExampleMessage> pulsarTemplate1;

    public ProducerController(PulsarTemplate<String> pulsarTemplate, PulsarTemplate<ExampleMessage> pulsarTemplate1) {
        this.pulsarTemplate = pulsarTemplate;
        this.pulsarTemplate1 = pulsarTemplate1;
    }

    @Value("${org.example.topic-no-schema}")
    public String topicwithNoSchema;

    @Value("${org.example.topic-schema}")
    public String topicWithSchema;

    @PostMapping("/exampleMessage")
    public ResponseEntity<String> sendMessageJson() {
        try {
            String messageId = pulsarTemplate.newMessage("This is a test message with UUID : "
                    + UUID.randomUUID()).withTopic(topicwithNoSchema).sendAsync().get().toString();
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
                    new ExampleMessage()).withTopic(topicWithSchema).sendAsync().get().toString();
            log.info("Sent Message ID: {}", messageId);
            return ResponseEntity.ok(messageId);
        } catch (PulsarClientException | ExecutionException | InterruptedException e) {
            log.error("Error sending message", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


}
