package com.example.kafka.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.kafka.config.KafkaProducerConfig.KAFKA_TOPIC_BATCH;


@Slf4j
@Component
public class BatchListener {

    @KafkaListener(topics = { KAFKA_TOPIC_BATCH }, containerFactory = "batchKafkaContainerFactory")
    public void listen(List<ConsumerRecord<?, ?>> consumerRecords, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();
        consumerRecords.stream().forEach(cr -> log.info(cr.toString()));
    }

}
