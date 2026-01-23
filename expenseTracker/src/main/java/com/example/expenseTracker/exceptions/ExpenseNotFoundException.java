package com.example.expenseTracker.exceptions;

public class ExpenseNotFoundException extends RuntimeException{
    public ExpenseNotFoundException(String msg){
        super(msg);
    }
}
