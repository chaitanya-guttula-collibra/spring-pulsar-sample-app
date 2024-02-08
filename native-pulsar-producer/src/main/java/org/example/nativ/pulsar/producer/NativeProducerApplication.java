package org.example.nativ.pulsar.producer;


import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.example.nativ.pulsar.producer.model.ExampleMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class NativeProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NativeProducerApplication.class, args);
    }

    public String topic = "persistent://playground/test/test.topic4.v1";

    @Bean
    public Producer<ExampleMessage> producer() throws PulsarClientException {
        try(PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar+ssl://pulsar-dev.pulsar-nprod-gcp.collibra-ops.com:6651")
                .authentication(
                        AuthenticationFactory.token(
                                "auth-token"))
                .build()) {
            return client.newProducer(Schema.JSON(ExampleMessage.class))
                    .topic(topic)
                    .create();
        }
    }
}
