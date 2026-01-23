package com.example.expenseTracker.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.expenseTracker.entities.User;


public interface UserRepository extends JpaRepository<User, Long>{
    public Optional<User> findByEmail(String email);
    public Boolean existsByEmail(String email);
}
