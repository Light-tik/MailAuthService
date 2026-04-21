package com.example.authservice.kafka;

import com.example.authservice.dto.request.VerifyRequest;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "kafka.topic.verification=verification-code"
})
@EmbeddedKafka(partitions = 1, topics = {"verification-code"})
class KafkaProducerTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Test
    void testProducerSendsMessage() {
        String email = "test@mail.com";
        String code = "12345";

        kafkaProducer.send(email, code);
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafka);
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.springframework.kafka.support.serializer.JsonDeserializer");
        consumerProps.put("spring.json.trusted.packages", "*");

        try (Consumer<String, VerifyRequest> consumer = new KafkaConsumer<>(consumerProps)) {
            embeddedKafka.consumeFromAnEmbeddedTopic(consumer, "verification-code");

            ConsumerRecord<String, VerifyRequest> singleRecord =
                    KafkaTestUtils.getSingleRecord(consumer, "verification-code", Duration.ofSeconds(5));

            assertThat(singleRecord.value()).isNotNull();
            assertThat(singleRecord.value().code()).isEqualTo(code);
            assertThat(singleRecord.value().email()).isEqualTo(email);
        }
    }
}
