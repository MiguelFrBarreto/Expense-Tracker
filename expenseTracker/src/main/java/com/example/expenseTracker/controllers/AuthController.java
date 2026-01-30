package com.example.expenseTracker.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.expenseTracker.dtos.JWTUserData;
import com.example.expenseTracker.dtos.LoginRequest;
import com.example.expenseTracker.dtos.RegisterRequest;
import com.example.expenseTracker.dtos.RegisterResponse;
import com.example.expenseTracker.dtos.UserResponse;
import com.example.expenseTracker.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        ResponseCookie authCookie = service.login(request);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, authCookie.toString()).build();
    }
    
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    @GetMapping("/@me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal JWTUserData userData) {
        return ResponseEntity.ok(service.me(userData));
    }
}
