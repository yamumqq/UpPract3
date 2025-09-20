package com.example.project2.service;

import com.example.project2.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TeacherService {
    
    // CRUD операции
    Teacher save(Teacher teacher);
    Optional<Teacher> findById(Long id);
    List<Teacher> findAll();
    Page<Teacher> findAll(Pageable pageable);
    Teacher update(Long id, Teacher teacher);
    void deleteById(Long id);
    void softDeleteById(Long id);
    
    // Поиск и фильтрация
    Page<Teacher> findBySearchTerm(String search, Pageable pageable);
    List<Teacher> findBySpecialization(String specialization);
    List<Teacher> findByMinExperience(Integer minExperience);
    Page<Teacher> findBySalaryBetween(BigDecimal minSalary, BigDecimal maxSalary, Pageable pageable);
    List<Teacher> findByHireDateBetween(LocalDate startDate, LocalDate endDate);
    List<Teacher> findByCourseId(Long courseId);
    
    // Статистика
    long count();
    
    // Восстановление
    void restoreById(Long id);
}
