package com.example.expenseTracker.dtos;

import com.example.expenseTracker.entities.Category;

public record CategoryResponse(
        Long id,
        String name) {

    public static CategoryResponse fromEntity(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName());
    }
}