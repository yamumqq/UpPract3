package com.example.project2.service;

import com.example.project2.model.Student;
import com.example.project2.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id)
                .filter(student -> !student.getIsDeleted());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findByIsDeletedFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Student> findAll(Pageable pageable) {
        return studentRepository.findByIsDeletedFalse(pageable);
    }
    
    @Override
    public Student update(Long id, Student student) {
        return studentRepository.findById(id)
                .map(existingStudent -> {
                    if (existingStudent.getIsDeleted()) {
                        throw new RuntimeException("Студент удален и не может быть изменен");
                    }
                    existingStudent.setFirstName(student.getFirstName());
                    existingStudent.setLastName(student.getLastName());
                    existingStudent.setEmail(student.getEmail());
                    existingStudent.setPhone(student.getPhone());
                    existingStudent.setBirthDate(student.getBirthDate());
                    existingStudent.setCourseYear(student.getCourseYear());
                    existingStudent.setGroup(student.getGroup());
                    return studentRepository.save(existingStudent);
                })
                .orElseThrow(() -> new RuntimeException("Студент не найден"));
    }
    
    @Override
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
    
    @Override
    public void softDeleteById(Long id) {
        studentRepository.findById(id)
                .ifPresent(student -> {
                    student.setIsDeleted(true);
                    studentRepository.save(student);
                });
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Student> findBySearchTerm(String search, Pageable pageable) {
        return studentRepository.findBySearchTerm(search, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Student> findByGroupId(Long groupId) {
        return studentRepository.findByGroupIdAndIsDeletedFalse(groupId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Student> findByCourseId(Long courseId) {
        return studentRepository.findByCourseIdAndIsDeletedFalse(courseId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Student> findByCourseYear(Integer courseYear) {
        return studentRepository.findByCourseYearAndIsDeletedFalse(courseYear);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Student> findByBirthDateBetween(LocalDate startDate, LocalDate endDate) {
        return studentRepository.findByBirthDateBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long count() {
        return studentRepository.countByIsDeletedFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByGroupId(Long groupId) {
        return studentRepository.countByGroupIdAndIsDeletedFalse(groupId);
    }
    
    @Override
    public void restoreById(Long id) {
        studentRepository.findById(id)
                .ifPresent(student -> {
                    student.setIsDeleted(false);
                    studentRepository.save(student);
                });
    }
}
