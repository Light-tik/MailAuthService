package com.example.mailservice.kafka;

import com.example.mailservice.dto.VerificationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"verification-code"})
class KafkaConsumerTest {

    @Autowired
    private KafkaTemplate<String, VerificationRequest> kafkaTemplate;

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Test
    void testConsumeMessage() {
        VerificationRequest request = new VerificationRequest("test@mail.com", "12345");
        kafkaTemplate.send("verification-code", request);
        kafkaTemplate.flush();

        await().atMost(Duration.ofSeconds(5)).untilAsserted(() ->
                assertThat(kafkaConsumer.getLastMessage())
                        .isNotNull()
                        .extracting(VerificationRequest::code)
                        .isEqualTo("12345")
        );
    }
}
