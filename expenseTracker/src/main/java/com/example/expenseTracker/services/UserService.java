package com.example.expenseTracker.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.expenseTracker.dtos.UserNameRequest;
import com.example.expenseTracker.dtos.UserPasswordRequest;
import com.example.expenseTracker.dtos.UserResponse;
import com.example.expenseTracker.entities.User;
import com.example.expenseTracker.exceptions.InvalidPasswordException;
import com.example.expenseTracker.exceptions.UserNotFoundException;
import com.example.expenseTracker.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    @Transactional(readOnly = true)
    public User findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return UserResponse.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public UserDetails findByEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return user;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return repository.findAll().stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    @Transactional
    public UserResponse updateName(UserNameRequest request, Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setName(request.name());

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