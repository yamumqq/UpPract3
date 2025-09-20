package com.example.project2.service;

import com.example.project2.model.Teacher;
import com.example.project2.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Override
    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Teacher> findById(Long id) {
        return teacherRepository.findById(id)
                .filter(teacher -> !teacher.getIsDeleted());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Teacher> findAll() {
        return teacherRepository.findByIsDeletedFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Teacher> findAll(Pageable pageable) {
        return teacherRepository.findByIsDeletedFalse(pageable);
    }
    
    @Override
    public Teacher update(Long id, Teacher teacher) {
        return teacherRepository.findById(id)
                .map(existingTeacher -> {
                    if (existingTeacher.getIsDeleted()) {
                        throw new RuntimeException("Преподаватель удален и не может быть изменен");
                    }
                    existingTeacher.setFirstName(teacher.getFirstName());
                    existingTeacher.setLastName(teacher.getLastName());
                    existingTeacher.setEmail(teacher.getEmail());
                    existingTeacher.setPhone(teacher.getPhone());
                    existingTeacher.setSpecialization(teacher.getSpecialization());
                    existingTeacher.setExperienceYears(teacher.getExperienceYears());
                    existingTeacher.setHireDate(teacher.getHireDate());
                    existingTeacher.setSalary(teacher.getSalary());
                    return teacherRepository.save(existingTeacher);
                })
                .orElseThrow(() -> new RuntimeException("Преподаватель не найден"));
    }
    
    @Override
    public void deleteById(Long id) {
        teacherRepository.deleteById(id);
    }
    
    @Override
    public void softDeleteById(Long id) {
        teacherRepository.findById(id)
                .ifPresent(teacher -> {
                    teacher.setIsDeleted(true);
                    teacherRepository.save(teacher);
                });
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Teacher> findBySearchTerm(String search, Pageable pageable) {
        return teacherRepository.findBySearchTerm(search, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Teacher> findBySpecialization(String specialization) {
        return teacherRepository.findBySpecializationAndIsDeletedFalse(specialization);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Teacher> findByMinExperience(Integer minExperience) {
        return teacherRepository.findByMinExperience(minExperience);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Teacher> findBySalaryBetween(BigDecimal minSalary, BigDecimal maxSalary, Pageable pageable) {
        return teacherRepository.findBySalaryBetween(minSalary, maxSalary, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Teacher> findByHireDateBetween(LocalDate startDate, LocalDate endDate) {
        return teacherRepository.findByHireDateBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Teacher> findByCourseId(Long courseId) {
        return teacherRepository.findByCourseIdAndIsDeletedFalse(courseId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long count() {
        return teacherRepository.countByIsDeletedFalse();
    }
    
    @Override
    public void restoreById(Long id) {
        teacherRepository.findById(id)
                .ifPresent(teacher -> {
                    teacher.setIsDeleted(false);
                    teacherRepository.save(teacher);
                });
    }
}
