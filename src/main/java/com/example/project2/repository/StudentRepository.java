package com.example.project2.repository;

import com.example.project2.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    

    List<Student> findByIsDeletedFalse();
    

    Page<Student> findByIsDeletedFalse(Pageable pageable);
    

    @Query("SELECT s FROM Student s WHERE s.isDeleted = false AND " +
           "(LOWER(s.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Student> findBySearchTerm(@Param("search") String search, Pageable pageable);
    

    Optional<Student> findByEmailAndIsDeletedFalse(String email);
    

    List<Student> findByGroupIdAndIsDeletedFalse(Long groupId);
    

    @Query("SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId AND s.isDeleted = false")
    List<Student> findByCourseIdAndIsDeletedFalse(@Param("courseId") Long courseId);
    

    List<Student> findByCourseYearAndIsDeletedFalse(Integer courseYear);
    
    // Поиск по диапазону дат рождения
    @Query("SELECT s FROM Student s WHERE s.isDeleted = false AND s.birthDate BETWEEN :startDate AND :endDate")
    List<Student> findByBirthDateBetween(@Param("startDate") LocalDate startDate, 
                                        @Param("endDate") LocalDate endDate);
    
    // Подсчет активных студентов
    long countByIsDeletedFalse();
    
    // Подсчет студентов в группе
    long countByGroupIdAndIsDeletedFalse(Long groupId);
}
