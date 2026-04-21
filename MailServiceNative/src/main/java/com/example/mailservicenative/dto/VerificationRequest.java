package com.example.mailservicenative.dto;

public record VerificationRequest(
        String email,
        String code
) {
}
