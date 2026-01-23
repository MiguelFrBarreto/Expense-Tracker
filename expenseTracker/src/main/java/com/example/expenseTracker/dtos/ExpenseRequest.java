package com.example.expenseTracker.dtos;

import java.time.Instant;

import com.example.expenseTracker.enums.Type;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ExpenseRequest(
                @NotBlank(message = "Description must not be blank")
                String description,
                @NotNull(message = "Amount must not be null")
                @Positive(message = "Amount must be positive")
                Double amount,
                @NotNull(message = "Date must not be null") 
                Instant date,
                @NotNull(message = "Type must not be null") 
                Type type,
                @Positive(message = "Category id must be positive") 
                @NotNull(message = "Category id must not be null") 
                Long categoryId) {

}
