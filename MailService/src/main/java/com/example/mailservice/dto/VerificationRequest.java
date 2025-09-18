package com.example.mailservice.dto;

public record VerificationRequest(
        String email,
        String code
) {
}
