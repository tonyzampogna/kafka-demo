package com.example.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.Collection;
import java.util.Map;


@Slf4j
@Configuration
public class KafkaContainerConfig {

    private static final String CONSUMER_GROUP_NAME_DEFAULT = "default-consumer";
    private static final String CONSUMER_GROUP_NAME_BATCH = "batch-consumer";

    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Qualify this bean with "kafkaListenerContainerFactory". Or, you can name it something
     * else and disable the KafkaAutoConfiguration class by adding the class to the Spring
     * autoconfigure exclusions.
     *
     *     spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
     *
     */
    @Bean
    @Qualifier("kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            @Qualifier("kafkaListenerConsumerFactory") ConsumerFactory<String, String> defaultConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(defaultConsumerFactory);
        return factory;
    }

    @Bean("kafkaListenerConsumerFactory")
    public ConsumerFactory<String, String> kafkaListenerConsumerFactory() {
        Map<String, Object> consumerConfigs = applicationContext.getBean("consumerConfigs", Map.class);
        consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_NAME_DEFAULT);
        return new DefaultKafkaConsumerFactory<>(consumerConfigs);
    }

    @Bean("batchKafkaContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> batchKafkaContainerFactory(
            @Qualifier("batchKafkaConsumerFactory") ConsumerFactory<String, String> batchKafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(batchKafkaConsumerFactory);
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setConsumerRebalanceListener(new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                for (TopicPartition partition : partitions) {
                    log.info("Partition Revoked - Partition: {}, Topic: {}", partition.partition(), partition.topic());
                }
            }
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                for (TopicPartition partition : partitions) {
                    log.info("Partition Assigned - Partition: {}, Topic: {}", partition.partition(), partition.topic());
                }
            }
        });
        return factory;
    }

    @Bean("batchKafkaConsumerFactory")
    public ConsumerFactory<String, String> batchKafkaConsumerFactory() {
        Map<String, Object> consumerConfigs = applicationContext.getBean("consumerConfigs", Map.class);
        consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_NAME_BATCH);
        consumerConfigs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return new DefaultKafkaConsumerFactory<>(consumerConfigs);
    }

}
