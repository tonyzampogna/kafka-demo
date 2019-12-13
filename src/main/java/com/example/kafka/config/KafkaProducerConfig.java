package com.example.kafka.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;


@Configuration
public class KafkaProducerConfig {

    public static final String KAFKA_TOPIC_DEFAULT = "default-topic";
    public static final String KAFKA_TOPIC_BATCH = "batch-topic";

    @Autowired
    private ApplicationContext applicationContext;


    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> producerConfigs = applicationContext.getBean("producerConfigs", Map.class);
        return new DefaultKafkaProducerFactory<>(producerConfigs);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(
            @Qualifier("producerFactory") ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}
