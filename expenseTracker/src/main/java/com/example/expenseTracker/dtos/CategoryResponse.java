package com.example.expenseTracker.dtos;

import com.example.expenseTracker.entities.Category;

public record CategoryResponse(
        Long id,
        String name,
        Long userId) {

    public static CategoryResponse fromEntity(Category category) {
        Long id = (category.getUser() == null) ? null : category.getUser().getId();

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                id);
    }
}