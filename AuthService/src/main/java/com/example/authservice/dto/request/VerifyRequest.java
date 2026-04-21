package com.example.authservice.dto.request;

import jakarta.validation.constraints.Email;

public record VerifyRequest(

        @Email
        String email,

        String code
) {
}
