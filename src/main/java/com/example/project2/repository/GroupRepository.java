package com.example.project2.repository;

import com.example.project2.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    

    List<Group> findByIsDeletedFalse();
    

    Page<Group> findByIsDeletedFalse(Pageable pageable);
    

    @Query("SELECT g FROM Group g WHERE g.isDeleted = false AND " +
           "LOWER(g.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Group> findBySearchTerm(@Param("search") String search, Pageable pageable);
    

    Optional<Group> findByNameAndIsDeletedFalse(String name);
    

    List<Group> findByCourseYearAndIsDeletedFalse(Integer courseYear);
    

    Page<Group> findByCourseYearAndIsDeletedFalse(Integer courseYear, Pageable pageable);
    

    long countByIsDeletedFalse();
    
    // Подсчет групп по году обучения
    long countByCourseYearAndIsDeletedFalse(Integer courseYear);
}
