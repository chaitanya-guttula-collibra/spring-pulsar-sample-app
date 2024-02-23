package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.CompressionType;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClientException;
import org.example.model.ExampleMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/send")
@Slf4j
public class ProducerController {

    public static final String ERROR_SENDING_MESSAGE = "Error sending message";
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
            log.error(ERROR_SENDING_MESSAGE, e);
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
            log.error(ERROR_SENDING_MESSAGE, e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/exampleSpringBatchProducer")
    public ResponseEntity<String> sendMessageWithSpringBatchProducer(@RequestParam Integer count) {

        try {
            List<CompletableFuture<MessageId>> futList = IntStream.range(0, count).parallel()
                    .mapToObj(i -> {
                        CompletableFuture<MessageId> mbs = null;
                        try {
                            mbs = pulsarTemplate1.newMessage(new ExampleMessage()).withTopic(topicWithSchema).withProducerCustomizer(producer -> {
                                producer.enableBatching(true);
                                producer.compressionType(CompressionType.LZ4);
                                producer.blockIfQueueFull(true);
                                producer.batchingMaxMessages(1_000);
                                producer.batchingMaxPublishDelay(100, TimeUnit.MILLISECONDS);
                                producer.batchingMaxBytes(1024 * 1024);
                            }).sendAsync();
                        } catch (PulsarClientException e) {
                            log.error(ERROR_SENDING_MESSAGE, e);
                            throw new RuntimeException(e);
                        }
                        return mbs;
                    })
                    .toList();
            CompletableFuture.allOf(futList.toArray(new CompletableFuture[0])).get();
            log.info("Sent {} messages", futList.size());
            return ResponseEntity.ok("Sent "+ futList.size()+" messages");
        } catch (ExecutionException | InterruptedException e) {
            log.error(ERROR_SENDING_MESSAGE, e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
