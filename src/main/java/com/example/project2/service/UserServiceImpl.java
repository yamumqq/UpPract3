package com.example.project2.service;

import com.example.project2.model.User;
import com.example.project2.model.Role;
import com.example.project2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public User registerUser(String username, String email, String password, Role role) {
        if (existsByUsername(username)) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        
        if (existsByEmail(email)) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        
        return save(user);
    }
    
    @Override
    public User updateUser(Long id, String username, String email, Role role) {
        User user = findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        
        // Проверяем, не занято ли имя пользователя другим пользователем
        if (!user.getUsername().equals(username) && existsByUsername(username)) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        
        // Проверяем, не занят ли email другим пользователем
        if (!user.getEmail().equals(email) && existsByEmail(email)) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);
        
        return save(user);
    }
    
    @Override
    public void changePassword(Long id, String newPassword) {
        User user = findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        save(user);
    }
    
    @Override
    public void deactivateUser(Long id) {
        User user = findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        
        user.setIsActive(false);
        save(user);
    }
    
    @Override
    public void activateUser(Long id) {
        User user = findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        
        user.setIsActive(true);
        save(user);
    }
}
