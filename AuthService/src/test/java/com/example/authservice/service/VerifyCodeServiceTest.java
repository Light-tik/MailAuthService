package com.example.authservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerifyCodeServiceTest {

    @InjectMocks
    private VerifyCodeService verifyCodeService;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void saveCode() {
        String email = "test@mail.com";
        String code = "123456";
        Duration ttl = Duration.ofMinutes(5);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        verifyCodeService.saveCode(email, code, ttl);

        verify(valueOperations).set(email, code, ttl);
    }

    @Test
    void getCode() {
        String email = "test@mail.com";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(email)).thenReturn("123456");

        String result = verifyCodeService.getCode(email);

        assertThat(result).isEqualTo("123456");
    }

    @Test
    void removeCode() {
        String email = "test@mail.com";

        verifyCodeService.removeCode(email);

        verify(redisTemplate).delete(email);
    }
}
