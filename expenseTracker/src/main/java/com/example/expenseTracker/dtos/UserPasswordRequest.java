package com.example.expenseTracker.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserPasswordRequest(
        @NotBlank(message = "Old password must not be blank") String oldPassword,
        @NotBlank(message = "New password must not be blank") String newPassword) {

}
