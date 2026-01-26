package com.example.expenseTracker.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ExpenseUpdateRequest(
                @NotBlank(message = "Description must not be blank") String description,
                @NotNull(message = "Amount must not be null") @Positive(message = "Amount must be positive") Double amount,
                @Positive(message = "Category id must be positive") @NotNull(message = "Category id must not be null") Long categoryId) {
}
