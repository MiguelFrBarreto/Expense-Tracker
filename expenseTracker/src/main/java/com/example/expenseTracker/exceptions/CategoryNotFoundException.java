package com.example.expenseTracker.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String msg){
        super(msg);
    }
}
