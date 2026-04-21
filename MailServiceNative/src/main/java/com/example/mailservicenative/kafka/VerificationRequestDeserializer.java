package com.example.mailservicenative.kafka;

import com.example.mailservicenative.dto.VerificationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class VerificationRequestDeserializer implements Deserializer<VerificationRequest> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public VerificationRequest deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        try {
            return objectMapper.readValue(data, VerificationRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize VerificationRequest", e);
        }
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
