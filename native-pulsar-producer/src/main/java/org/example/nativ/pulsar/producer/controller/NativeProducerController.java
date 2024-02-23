package org.example.nativ.pulsar.producer.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.CompressionType;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.TypedMessageBuilder;
import org.example.nativ.pulsar.producer.model.ExampleMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/send")
@Slf4j
public class NativeProducerController {

    private final Producer<ExampleMessage> producer;

    @Value("${org.example.topic}")
    public String topic;
    private final PulsarClient pulsarClient;

    public NativeProducerController(PulsarClient pulsarClient, Producer<ExampleMessage> producer) {
        this.producer = producer;
        this.pulsarClient = pulsarClient;
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

    @PostMapping("/exampleBatchProducer")
    public ResponseEntity<String> sendMessageJsonWithBatchProducer(@RequestParam Integer count) {

        try(Producer<ExampleMessage> messageproducer = pulsarClient.newProducer(Schema.JSON(ExampleMessage.class))
                .topic(topic)
                .enableChunking(false)
                .blockIfQueueFull(true)
                .enableBatching(true)
                .compressionType(CompressionType.LZ4)
                .batchingMaxMessages(1_000)
                .batchingMaxPublishDelay(100, TimeUnit.MILLISECONDS)
                .batchingMaxBytes(1024 * 1024)
                .create()) {

            List<CompletableFuture<MessageId>> futList = IntStream.range(0, count).parallel()
                    .mapToObj(i -> {
                        TypedMessageBuilder<ExampleMessage> mbs = messageproducer.newMessage(Schema.JSON(ExampleMessage.class));
                        mbs.value(new ExampleMessage())
                                .eventTime(Instant.now().toEpochMilli());
                        return mbs.sendAsync();
                    })
                    .toList();
            messageproducer.flush();
            CompletableFuture.allOf(futList.toArray(new CompletableFuture[0])).get();
            log.info("Sent {} messages", futList.size());
            return ResponseEntity.ok("Sent "+ futList.size()+" messages");
        } catch (ExecutionException | InterruptedException | PulsarClientException e) {
            log.error("Error sending message", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


}
