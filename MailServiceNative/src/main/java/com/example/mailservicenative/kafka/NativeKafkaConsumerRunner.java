package com.example.mailservicenative.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NativeKafkaConsumerRunner {

    private final NativeKafkaConsumer consumer;

    public NativeKafkaConsumerRunner(@Value("${kafka.bootstrap-servers}") String brokers,
                                     @Value("${kafka.topic.verification}") String topic) {
        this.consumer = new NativeKafkaConsumer(brokers, topic);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start(){
        Thread consumerThread = new Thread(consumer::run, "native-kafka-consumer-thread");
        consumerThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down native kafka-consumer");
            consumer.shutdown();
        }));
    }
}

