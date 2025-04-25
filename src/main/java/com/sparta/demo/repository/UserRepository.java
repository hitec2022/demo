package com.sparta.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
    User findByUserEmail(String userEmail);
}
