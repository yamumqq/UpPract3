package com.example.project2.service;

import com.example.project2.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    
    // CRUD операции
    Group save(Group group);
    Optional<Group> findById(Long id);
    List<Group> findAll();
    Page<Group> findAll(Pageable pageable);
    Group update(Long id, Group group);
    void deleteById(Long id);
    void softDeleteById(Long id);
    
    // Поиск и фильтрация
    Page<Group> findBySearchTerm(String search, Pageable pageable);
    List<Group> findByCourseYear(Integer courseYear);
    Page<Group> findByCourseYear(Integer courseYear, Pageable pageable);
    
    // Статистика
    long count();
    long countByCourseYear(Integer courseYear);
    
    // Восстановление
    void restoreById(Long id);
}
