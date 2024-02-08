package org.example.nativ.pulsar.producer.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.Schema;
import org.example.nativ.pulsar.producer.model.ExampleMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/send")
@Slf4j
public class NativeProducerController {

    private final Producer<ExampleMessage> producer;

    public NativeProducerController(Producer<ExampleMessage> producer) {
        this.producer = producer;
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
