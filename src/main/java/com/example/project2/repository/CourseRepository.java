package com.example.project2.repository;

import com.example.project2.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    

    List<Course> findByIsDeletedFalse();
    

    Page<Course> findByIsDeletedFalse(Pageable pageable);
    

    @Query("SELECT c FROM Course c WHERE c.isDeleted = false AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Course> findBySearchTerm(@Param("search") String search, Pageable pageable);
    

    @Query("SELECT c FROM Course c WHERE c.isDeleted = false AND c.hours BETWEEN :minHours AND :maxHours")
    Page<Course> findByHoursBetween(@Param("minHours") Integer minHours, 
                                   @Param("maxHours") Integer maxHours, 
                                   Pageable pageable);
    

    @Query("SELECT c FROM Course c WHERE c.isDeleted = false AND c.price BETWEEN :minPrice AND :maxPrice")
    Page<Course> findByPriceBetween(@Param("minPrice") BigDecimal minPrice, 
                                   @Param("maxPrice") BigDecimal maxPrice, 
                                   Pageable pageable);
    
    // Поиск по преподавателю
    @Query("SELECT c FROM Course c JOIN c.teachers t WHERE t.id = :teacherId AND c.isDeleted = false")
    List<Course> findByTeacherIdAndIsDeletedFalse(@Param("teacherId") Long teacherId);
    
    // Поиск по студенту
    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId AND c.isDeleted = false")
    List<Course> findByStudentIdAndIsDeletedFalse(@Param("studentId") Long studentId);
    
    // Подсчет активных курсов
    long countByIsDeletedFalse();
}
