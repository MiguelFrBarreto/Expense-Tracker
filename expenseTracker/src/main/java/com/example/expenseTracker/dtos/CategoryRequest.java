package com.example.expenseTracker.dtos;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "Name must not be blank") 
        String name
) {
}
