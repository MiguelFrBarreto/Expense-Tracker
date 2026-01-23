package com.example.expenseTracker.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.expenseTracker.dtos.ExpenseRequest;
import com.example.expenseTracker.dtos.ExpenseResponse;
import com.example.expenseTracker.dtos.ExpenseUpdateRequest;
import com.example.expenseTracker.entities.Expense;
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
    public ExpenseResponse findById(Long id) {
        Expense expense = repository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));

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

    //Pegar userId pelo controller com @AuthenticationPrincipal
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
    public ExpenseResponse update(ExpenseUpdateRequest request, Long id) {
        Expense expense = repository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));

        if (request.description() != null) {
            expense.setDescription(request.description());
        }
        if (request.amount() != null && request.amount() > 0) {
            expense.setAmount(request.amount());
        }
        if (request.categoryId() != null && request.categoryId() > 0) {
            expense.setCategory(categoryService.findEntityById(request.categoryId()));
        }
        return ExpenseResponse.fromEntity(expense);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ExpenseNotFoundException("Expense not found");
        }

        repository.deleteById(id);
    }
}
