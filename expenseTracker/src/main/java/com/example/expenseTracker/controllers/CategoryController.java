package com.example.expenseTracker.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.expenseTracker.dtos.CategoryRequest;
import com.example.expenseTracker.dtos.CategoryResponse;
import com.example.expenseTracker.dtos.CategoryUpdateRequest;
import com.example.expenseTracker.dtos.JWTUserData;
import com.example.expenseTracker.services.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @GetMapping()
    public ResponseEntity<List<CategoryResponse>> findAllByUserId(@AuthenticationPrincipal JWTUserData userData){
        return ResponseEntity.ok(service.findAllByUserId(userData.userId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id, @AuthenticationPrincipal JWTUserData userData) {
        return ResponseEntity.ok(service.findById(id, userData.userId()));
    }
 
    @PostMapping()
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request, @AuthenticationPrincipal JWTUserData userData) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request, userData.userId()));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, @AuthenticationPrincipal JWTUserData userData){
        service.deleteById(id, userData.userId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody CategoryUpdateRequest request, @PathVariable Long id, @AuthenticationPrincipal JWTUserData userData){
        service.update(request, id, userData.userId());
        return ResponseEntity.noContent().build();
    }

    //Global category (has not user)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/global")
    public ResponseEntity<CategoryResponse> createGlobalCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request, null));
    }
    
}
