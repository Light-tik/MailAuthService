package com.example.authservice.dto.request;

import jakarta.validation.constraints.Email;

public record RegisterRequest(

        @Email
        String email
) {
}
