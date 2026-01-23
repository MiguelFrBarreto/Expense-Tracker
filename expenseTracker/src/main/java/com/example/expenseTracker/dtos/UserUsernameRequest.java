package com.example.expenseTracker.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserUsernameRequest(
                @NotBlank(message = "Username must not be blank") String username) {

}
