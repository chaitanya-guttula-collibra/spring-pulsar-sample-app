package org.example.nativ.pulsar.producer;


import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.example.nativ.pulsar.producer.model.ExampleMessage;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${org.example.topic}")
    public String topic;

    @Value("${org.example.pulsar.client.authentication.param.token}")
    public String token;

    @Value("${org.example.pulsar.client.service-url}")
    public String serviceUrl;

    @Bean
    public Producer<ExampleMessage> producer() throws PulsarClientException {
        try {
            PulsarClient client = PulsarClient.builder()
                    .serviceUrl(serviceUrl)
                    .authentication(
                            AuthenticationFactory.token(token))
                    .build();
            return client.newProducer(Schema.JSON(ExampleMessage.class))
                    .topic(topic)
                    .create();
        } catch (PulsarClientException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
