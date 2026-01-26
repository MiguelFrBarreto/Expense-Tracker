package com.example.expenseTracker.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.expenseTracker.dtos.JWTUserData;
import com.example.expenseTracker.dtos.UserNameRequest;
import com.example.expenseTracker.dtos.UserPasswordRequest;
import com.example.expenseTracker.dtos.UserResponse;
import com.example.expenseTracker.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping()
    public ResponseEntity<UserResponse> findById(@AuthenticationPrincipal JWTUserData userData) {
        return ResponseEntity.ok(service.findById(userData.userId()));
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteById(@AuthenticationPrincipal JWTUserData userData) {
        service.deleteById(userData.userId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/name")
    public ResponseEntity<Void> patchName(@Valid @RequestBody UserNameRequest request, @AuthenticationPrincipal JWTUserData userData) {
        service.updateName(request, userData.userId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> patchPassword(@Valid @RequestBody UserPasswordRequest request,@AuthenticationPrincipal JWTUserData userData) {
        service.updatePassword(request, userData.userId());
        return ResponseEntity.noContent().build();
    }
}
