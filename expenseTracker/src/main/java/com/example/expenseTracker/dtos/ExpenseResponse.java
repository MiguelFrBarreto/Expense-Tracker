package com.example.expenseTracker.dtos;

import java.time.Instant;

import com.example.expenseTracker.entities.Expense;
import com.example.expenseTracker.enums.Type;

public record ExpenseResponse(
        Long id,
        String description,
        Double amount,
        Instant date,
        Type type,
        CategoryResponse category) {
    public static ExpenseResponse fromEntity(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getDate(),
                expense.getType(),
                CategoryResponse.fromEntity(expense.getCategory()));
    }
}
