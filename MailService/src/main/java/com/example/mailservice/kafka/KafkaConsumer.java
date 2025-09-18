package com.example.mailservice.kafka;

import com.example.mailservice.dto.VerificationRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class KafkaConsumer {

    private VerificationRequest lastMessage;

    @KafkaListener(
            topics = "verification-code",
            groupId = "mailer-service"
    )
    public void listen(VerificationRequest request) {
        System.out.println("Sending a letter to " + request.email() + " with a code " + request.code());
        log.info("Sending a letter to {} with a code {}", request.email(), request.code());
        this.lastMessage = request;
    }
}
