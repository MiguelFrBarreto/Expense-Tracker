package com.example.expenseTracker.dtos;

public record ExpenseUpdateRequest(
        String description,
        Double amount,
        Long categoryId) {

}
