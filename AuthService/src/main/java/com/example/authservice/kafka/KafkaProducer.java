package com.example.authservice.kafka;

import com.example.authservice.dto.request.VerifyRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, VerifyRequest> kafkaTemplate;

    @Value("${kafka.topic.verification}")
    private String topic;

    public void send(String email, String code) {
        kafkaTemplate.send(topic, email, new VerifyRequest(email, code));
        log.info("Email and code sent");
    }
}
