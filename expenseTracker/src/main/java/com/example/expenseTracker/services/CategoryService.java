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
import com.example.expenseTracker.exceptions.CategoryNotFoundException;
import com.example.expenseTracker.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;

    @Transactional(readOnly = true)
    public Category findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

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
    public CategoryResponse create(CategoryRequest request, User user) {
        Category category = new Category(request.name(), null);
        if (!(user == null)){
            category.setUser(user);
        }

        return CategoryResponse.fromEntity(repository.save(category));
    }

    @Transactional
    public CategoryResponse update(CategoryUpdateRequest request, Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        category.setName(request.name());

        return CategoryResponse.fromEntity(category);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new CategoryNotFoundException("Category not found");
        }

        repository.deleteById(id);
    }
}
