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
    public List<CategoryResponse> findAll(Long userId) {
        List<Category> categories = new ArrayList<>();

        categories.addAll(repository.findByUserIsNull());
        categories.addAll(repository.findByUserId(userId));

        return categories.stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAllByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAllGlobalCategories() {
        return repository.findByUserIsNull().stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }

    @Transactional()
    public CategoryResponse create(CategoryRequest request, Long userId) {
        User user = userService.findEntityById(userId);

        Category category = new Category(request.name(), user);

        return CategoryResponse.fromEntity(repository.save(category));
    }

    @Transactional()
    public CategoryResponse createGlobal(CategoryRequest request) {
        Category category = new Category(request.name(), null);

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
