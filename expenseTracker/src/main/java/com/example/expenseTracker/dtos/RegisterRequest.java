package com.example.expenseTracker.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "Username must not be blank") 
        String username,
        @NotBlank(message = "Email must not be blank") 
        @Email(message = "Invalid email") 
        String email,
        @NotBlank(message = "Password must not be blank") 
        String password) {

}
