package com.example.mailservicenative.kafka;

import com.example.mailservicenative.dto.VerificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
public class NativeKafkaConsumer {

    private final KafkaConsumer<String, VerificationRequest> kafkaConsumer;

    private final AtomicBoolean stop = new AtomicBoolean(false);

    public NativeKafkaConsumer(String bootstrapServers, String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "native-mailer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, VerificationRequestDeserializer.class.getName());
        props.put("spring.json.trusted.packages", "*");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        this.kafkaConsumer = new KafkaConsumer<>(props);
        this.kafkaConsumer.subscribe(Collections.singletonList(topic));
    }

    public  void shutdown() {
        stop.set(true);
    }

    public void run() {
        try {
            while (!stop.get()) {
                ConsumerRecords<String, VerificationRequest> records = kafkaConsumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, VerificationRequest> consumerRecord : records) {
                    VerificationRequest verificationRequest = consumerRecord.value();
                    log.info("Received Verification Request: {}", verificationRequest);
                    System.out.println("Received Verification Request: " + verificationRequest);
                }
            }
        } catch (Exception e) {
            log.error("Error occurred when consuming records", e);
        } finally {
            kafkaConsumer.close();
        }
    }
}
