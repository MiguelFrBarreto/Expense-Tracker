package com.example.expenseTracker.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.expenseTracker.entities.User;
import com.example.expenseTracker.services.CategoryService;
import com.example.expenseTracker.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;
    private final UserService userService;
    
    @GetMapping()
    public ResponseEntity<List<CategoryResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    //implements @me
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<CategoryResponse>> findAllByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(service.findAllByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
 
    //implements @me
    @PostMapping("/{userId}")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request, @PathVariable Long userId) {
        User user = userService.findEntityById(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request, user));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody CategoryUpdateRequest request, Long id){
        service.update(request, id);
        return ResponseEntity.noContent().build();
    }

    //Global category (has not user)
    @PostMapping("/global")
    public ResponseEntity<CategoryResponse> createGlobalCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request, null));
    }
    
}
