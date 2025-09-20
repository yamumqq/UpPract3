package com.example.project2.service;

import com.example.project2.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    
    // CRUD операции
    Student save(Student student);
    Optional<Student> findById(Long id);
    List<Student> findAll();
    Page<Student> findAll(Pageable pageable);
    Student update(Long id, Student student);
    void deleteById(Long id);
    void softDeleteById(Long id);
    
    // Поиск и фильтрация
    Page<Student> findBySearchTerm(String search, Pageable pageable);
    List<Student> findByGroupId(Long groupId);
    List<Student> findByCourseId(Long courseId);
    List<Student> findByCourseYear(Integer courseYear);
    List<Student> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Статистика
    long count();
    long countByGroupId(Long groupId);
    
    // Восстановление
    void restoreById(Long id);
}
