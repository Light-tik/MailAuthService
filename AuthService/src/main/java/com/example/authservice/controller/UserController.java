package com.example.authservice.controller;

import com.example.authservice.dto.request.RegisterRequest;
import com.example.authservice.dto.request.VerifyRequest;
import com.example.authservice.dto.response.TokenResponse;
import com.example.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<TokenResponse> verifyEmail(@Valid @RequestBody VerifyRequest request){
        return ResponseEntity.ok(userService.verify(request));
    }
}
