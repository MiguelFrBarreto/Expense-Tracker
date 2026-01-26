package com.example.expenseTracker.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.expenseTracker.dtos.CategoryRequest;
import com.example.expenseTracker.dtos.CategoryResponse;
import com.example.expenseTracker.dtos.CategoryUpdateRequest;
import com.example.expenseTracker.entities.Category;
import com.example.expenseTracker.entities.User;
import com.example.expenseTracker.exceptions.AccessDeniedException;
import com.example.expenseTracker.exceptions.CategoryNotFoundException;
import com.example.expenseTracker.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Category findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id, Long userId) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        if (category.getUser() != null && !category.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Forbidden");
        }

        return CategoryResponse.fromEntity(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return repository.findAll().stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAllByUserId(Long userId) {
        List<CategoryResponse> responses = new ArrayList<>();

        responses.addAll(repository.findByUserIsNull().stream()
                .map(CategoryResponse::fromEntity)
                .toList());

        responses.addAll(repository.findByUserId(userId).stream()
                .map(CategoryResponse::fromEntity)
                .toList());

        return responses;
    }

    @Transactional()
    public CategoryResponse create(CategoryRequest request, Long userId) {
        Category category = new Category(request.name(), null);

        if (userId != null) {
            User user = userService.findEntityById(userId);
            category.setUser(user);
        }

        return CategoryResponse.fromEntity(repository.save(category));
    }

    @Transactional
    public CategoryResponse update(CategoryUpdateRequest request, Long categoryId, Long userId) {
        Category category = repository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        if (category.getUser() == null || !category.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Forbidden");
        }

        category.setName(request.name());

        return CategoryResponse.fromEntity(category);
    }

    @Transactional
    public void deleteById(Long categoryId, Long userId) {
        Category category = repository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        if (category.getUser() == null || !category.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Forbidden");
        }

        repository.delete(category);
    }
}
