package com.example.expenseTracker.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.expenseTracker.services.ExpenseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService service;

    @GetMapping()
    public ResponseEntity<List<ExpenseResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ExpenseResponse>> findAllByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findAllByUserId(userId));
    }
    

    @PostMapping("/{userId}")
    public ResponseEntity<ExpenseResponse> create(@Valid @RequestBody ExpenseRequest request, @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request, userId));
    }

    @PatchMapping()
    public ResponseEntity<Void> update(@Valid @RequestBody ExpenseUpdateRequest request, @PathVariable Long userId) {
        service.update(request, userId);
        return ResponseEntity.noContent().build();
    }

}
