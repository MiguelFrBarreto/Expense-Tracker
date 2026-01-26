package com.example.expenseTracker.dtos;

import java.util.List;

import com.example.expenseTracker.entities.User;

public record UserResponse(
        Long id,
        String name,
        String email,
        List<ExpenseResponse> expenses,
        List<CategoryResponse> categories) {

    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getExpenses().stream()
                        .map(ExpenseResponse::fromEntity)
                        .toList(),
                user.getCategories().stream()
                        .map(CategoryResponse::fromEntity)
                        .toList());
                
    }
}
