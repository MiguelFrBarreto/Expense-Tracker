package com.example.expenseTracker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.expenseTracker.entities.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
        public List<Expense> findByUserId(Long userId);
}
