package com.example.project2.service;

import com.example.project2.model.Course;
import com.example.project2.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id)
                .filter(course -> !course.getIsDeleted());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return courseRepository.findByIsDeletedFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Course> findAll(Pageable pageable) {
        return courseRepository.findByIsDeletedFalse(pageable);
    }
    
    @Override
    public Course update(Long id, Course course) {
        return courseRepository.findById(id)
                .map(existingCourse -> {
                    if (existingCourse.getIsDeleted()) {
                        throw new RuntimeException("Курс удален и не может быть изменен");
                    }
                    existingCourse.setName(course.getName());
                    existingCourse.setDescription(course.getDescription());
                    existingCourse.setHours(course.getHours());
                    existingCourse.setPrice(course.getPrice());
                    return courseRepository.save(existingCourse);
                })
                .orElseThrow(() -> new RuntimeException("Курс не найден"));
    }
    
    @Override
    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }
    
    @Override
    public void softDeleteById(Long id) {
        courseRepository.findById(id)
                .ifPresent(course -> {
                    course.setIsDeleted(true);
                    courseRepository.save(course);
                });
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Course> findBySearchTerm(String search, Pageable pageable) {
        return courseRepository.findBySearchTerm(search, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Course> findByHoursBetween(Integer minHours, Integer maxHours, Pageable pageable) {
        return courseRepository.findByHoursBetween(minHours, maxHours, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Course> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return courseRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Course> findByTeacherId(Long teacherId) {
        return courseRepository.findByTeacherIdAndIsDeletedFalse(teacherId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Course> findByStudentId(Long studentId) {
        return courseRepository.findByStudentIdAndIsDeletedFalse(studentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long count() {
        return courseRepository.countByIsDeletedFalse();
    }
    
    @Override
    public void restoreById(Long id) {
        courseRepository.findById(id)
                .ifPresent(course -> {
                    course.setIsDeleted(false);
                    courseRepository.save(course);
                });
    }
}
