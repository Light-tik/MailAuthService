package com.example.authservice.dto.response;

public record TokenResponse(

        String accessToken,

        Long expiresIn
) {
}
