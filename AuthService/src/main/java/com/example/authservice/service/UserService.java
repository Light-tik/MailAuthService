package com.example.authservice.service;

import com.example.authservice.dto.request.RegisterRequest;
import com.example.authservice.dto.request.VerifyRequest;
import com.example.authservice.dto.response.TokenResponse;
import com.example.authservice.kafka.KafkaProducer;
import com.example.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;

    private final KafkaProducer kafkaProducer;

    private final VerifyCodeService verifyCodeService;

    @Value("${ttl.token}")
    private Long ttl;


    public String registerUser(RegisterRequest registerRequest) {
        String code = String.format("%06d", (int)(Math.random()*900000)+100000);
        verifyCodeService.saveCode(registerRequest.email(), code, Duration.ofMinutes(10));
        kafkaProducer.send(registerRequest.email(), code);
        log.info("Code sent to kafka for user: {}", registerRequest.email());
        return code;
    }

    public TokenResponse verify(VerifyRequest request) {
        String savedCode = verifyCodeService.getCode(request.email());
        if (savedCode == null || !savedCode.equals(request.code().trim())) {
            throw new IllegalArgumentException("Invalid or expired code");
        }
        verifyCodeService.removeCode(request.email());
        String token = jwtService.generateToken(request.email());
        log.info("Code checked and token generated: {}", token);
        return new TokenResponse(token,ttl);
    }
}
