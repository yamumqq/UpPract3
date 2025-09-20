package com.example.project2.service;

import com.example.project2.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CourseService {
    
    // CRUD операции
    Course save(Course course);
    Optional<Course> findById(Long id);
    List<Course> findAll();
    Page<Course> findAll(Pageable pageable);
    Course update(Long id, Course course);
    void deleteById(Long id);
    void softDeleteById(Long id);
    
    // Поиск и фильтрация
    Page<Course> findBySearchTerm(String search, Pageable pageable);
    Page<Course> findByHoursBetween(Integer minHours, Integer maxHours, Pageable pageable);
    Page<Course> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    List<Course> findByTeacherId(Long teacherId);
    List<Course> findByStudentId(Long studentId);
    
    // Статистика
    long count();
    
    // Восстановление
    void restoreById(Long id);
}
