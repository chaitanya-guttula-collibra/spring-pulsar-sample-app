package org.example.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfiguration {

    private static final String EXAMPLE_TOPIC = "persistent://playground/test/test.topic4.v1";

    private String getTopicName() {
        return EXAMPLE_TOPIC;
    }

    @Bean
    @Qualifier("topicName")
    public String getTopic() {
        return getTopicName();
    }

}
