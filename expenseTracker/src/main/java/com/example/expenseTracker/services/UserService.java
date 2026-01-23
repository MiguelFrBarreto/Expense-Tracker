package com.example.expenseTracker.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.expenseTracker.dtos.CategoryResponse;
import com.example.expenseTracker.dtos.ExpenseResponse;
import com.example.expenseTracker.dtos.UserPasswordRequest;
import com.example.expenseTracker.dtos.UserRequest;
import com.example.expenseTracker.dtos.UserResponse;
import com.example.expenseTracker.dtos.UserUsernameRequest;
import com.example.expenseTracker.entities.User;
import com.example.expenseTracker.exceptions.EmailAlreadyUsedException;
import com.example.expenseTracker.exceptions.InvalidPasswordException;
import com.example.expenseTracker.exceptions.UserNotFoundException;
import com.example.expenseTracker.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    public User findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<CategoryResponse> categories = new ArrayList<>();

        categories.addAll(categoryService.findAllByUserId(user.getId()));

        UserResponse userResponse = new UserResponse(
            user.getId(),
            user.getUsername(), 
            user.getEmail(), 
            user.getExpenses().stream()
                .map(ExpenseResponse::fromEntity)
                .toList(),
            categories);

        return userResponse;
    }

    @Transactional(readOnly = true)
    public UserResponse findByEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return UserResponse.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return repository.findAll().stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        if(repository.existsByEmail(request.email())){
            throw new EmailAlreadyUsedException("Email already used");
        }
        User user = new User(request.username(), request.email(), request.password());
        return UserResponse.fromEntity(repository.save(user));
    }

    @Transactional
    public UserResponse updateUsername(UserUsernameRequest request, Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setUsername(request.username());

        return UserResponse.fromEntity(user);
    }

    @Transactional
    public UserResponse updatePassword(UserPasswordRequest request, Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!request.oldPassword().equals(user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        user.setPassword(request.newPassword());

        return UserResponse.fromEntity(user);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        repository.deleteById(id);
    }
}