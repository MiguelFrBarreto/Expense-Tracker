package com.example.expenseTracker.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.expenseTracker.dtos.ExpenseRequest;
import com.example.expenseTracker.dtos.ExpenseResponse;
import com.example.expenseTracker.dtos.ExpenseUpdateRequest;
import com.example.expenseTracker.entities.Category;
import com.example.expenseTracker.entities.Expense;
import com.example.expenseTracker.exceptions.AccessDeniedException;
import com.example.expenseTracker.exceptions.ExpenseNotFoundException;
import com.example.expenseTracker.repositories.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository repository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    public ExpenseResponse findById(Long id, Long userId) {
        Expense expense = repository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));

        if (!expense.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Forbidden");
        }

        return ExpenseResponse.fromEntity(expense);
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> findAll() {
        return repository.findAll().stream()
                .map(ExpenseResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> findAllByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(ExpenseResponse::fromEntity)
                .toList();
    }

    @Transactional
    public ExpenseResponse create(ExpenseRequest request, Long userId) {
        Expense expense = new Expense(
                request.description(),
                request.amount(),
                request.date(),
                request.type(),
                userService.findEntityById(userId),
                categoryService.findEntityById(request.categoryId()));

        repository.save(expense);
        return ExpenseResponse.fromEntity(expense);
    }

    @Transactional
    public ExpenseResponse update(ExpenseUpdateRequest request, Long expenseId, Long userId) {
        Expense expense = repository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));

        if (!expense.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Forbidden");
        }

        Category category = categoryService.findEntityById(request.categoryId());

        if (category.getUser() == null || !category.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Forbidden");
        }

        expense.setDescription(request.description());
        expense.setAmount(request.amount());
        expense.setCategory(category);

        return ExpenseResponse.fromEntity(expense);
    }

    @Transactional
    public void deleteById(Long expenseId, Long userId) {
        Expense expense = repository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not foubd"));

        if (!expense.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Forbidden");
        }

        repository.delete(expense);
    }
}
