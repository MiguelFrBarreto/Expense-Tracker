package com.example.expenseTracker.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.expenseTracker.dtos.ExpenseRequest;
import com.example.expenseTracker.dtos.ExpenseResponse;
import com.example.expenseTracker.dtos.ExpenseUpdateRequest;
import com.example.expenseTracker.dtos.JWTUserData;
import com.example.expenseTracker.services.ExpenseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService service;

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> findById(@PathVariable Long id, @AuthenticationPrincipal JWTUserData userData) {
        return ResponseEntity.ok(service.findById(id, userData.userId()));
    }

    @GetMapping()
    public ResponseEntity<List<ExpenseResponse>> findAllByUserId(@AuthenticationPrincipal JWTUserData userData) {
        return ResponseEntity.ok(service.findAllByUserId(userData.userId()));
    }
    
    @PostMapping()
    public ResponseEntity<ExpenseResponse> create(@Valid @RequestBody ExpenseRequest request, @AuthenticationPrincipal JWTUserData userData) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request, userData.userId()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody ExpenseUpdateRequest request, @PathVariable Long id, @AuthenticationPrincipal JWTUserData userData) {
        service.update(request, id, userData.userId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, @AuthenticationPrincipal JWTUserData userData){
        service.deleteById(id, userData.userId());
        return ResponseEntity.noContent().build();
    }

}
