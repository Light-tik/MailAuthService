package com.example.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerifyCodeService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveCode(String email, String code, Duration ttl) {
        redisTemplate.opsForValue().set(email, code, ttl);
        log.info("Saved verification code to {}", email);
    }

    public String getCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public void removeCode(String email) {
        redisTemplate.delete(email);
        log.info("Removed email: {}", email);
    }
}
