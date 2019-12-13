package com.example.kafka.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.example.kafka.config.KafkaProducerConfig.KAFKA_TOPIC_DEFAULT;


@Slf4j
@Component
public class DefaultListener {

    @KafkaListener(topics = { KAFKA_TOPIC_DEFAULT }, containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<?, ?> consumerRecord) {
        log.info(consumerRecord.toString());
    }

}
