package com.example.project2.service;

import com.example.project2.model.User;
import com.example.project2.model.Role;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    User save(User user);
    
    Optional<User> findById(Long id);
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    List<User> findAll();
    
    void deleteById(Long id);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    User registerUser(String username, String email, String password, Role role);
    
    User updateUser(Long id, String username, String email, Role role);
    
    void changePassword(Long id, String newPassword);
    
    void deactivateUser(Long id);
    
    void activateUser(Long id);
}

