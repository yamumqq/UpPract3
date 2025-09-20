package com.example.project2.repository;

import com.example.project2.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    

    List<Teacher> findByIsDeletedFalse();
    

    Page<Teacher> findByIsDeletedFalse(Pageable pageable);
    

    @Query("SELECT t FROM Teacher t WHERE t.isDeleted = false AND " +
           "(LOWER(t.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(t.lastName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Teacher> findBySearchTerm(@Param("search") String search, Pageable pageable);
    

    Optional<Teacher> findByEmailAndIsDeletedFalse(String email);
    

    List<Teacher> findBySpecializationAndIsDeletedFalse(String specialization);
    

    @Query("SELECT t FROM Teacher t WHERE t.isDeleted = false AND t.experienceYears >= :minExperience")
    List<Teacher> findByMinExperience(@Param("minExperience") Integer minExperience);
    

    @Query("SELECT t FROM Teacher t WHERE t.isDeleted = false AND t.salary BETWEEN :minSalary AND :maxSalary")
    Page<Teacher> findBySalaryBetween(@Param("minSalary") BigDecimal minSalary, 
                                     @Param("maxSalary") BigDecimal maxSalary, 
                                     Pageable pageable);
    
    // Поиск по дате найма
    @Query("SELECT t FROM Teacher t WHERE t.isDeleted = false AND t.hireDate BETWEEN :startDate AND :endDate")
    List<Teacher> findByHireDateBetween(@Param("startDate") LocalDate startDate, 
                                       @Param("endDate") LocalDate endDate);
    
    // Поиск по курсу
    @Query("SELECT t FROM Teacher t JOIN t.courses c WHERE c.id = :courseId AND t.isDeleted = false")
    List<Teacher> findByCourseIdAndIsDeletedFalse(@Param("courseId") Long courseId);
    
    // Подсчет активных преподавателей
    long countByIsDeletedFalse();
}
