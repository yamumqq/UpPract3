package com.example.project2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название курса не может быть пустым")
    @Size(min = 3, max = 100, message = "Название курса должно содержать от 3 до 100 символов")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    @Column(name = "description")
    private String description;

    @Min(value = 1, message = "Количество часов должно быть не менее 1")
    @Max(value = 1000, message = "Количество часов не должно превышать 1000")
    @Column(name = "hours", nullable = false)
    private Integer hours;

    @DecimalMin(value = "0.0", message = "Стоимость не может быть отрицательной")
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    // Связь M:M с Student
    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();

    // Связь M:M с Teacher
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "course_teachers",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Set<Teacher> teachers = new HashSet<>();

    // Конструкторы
    public Course() {}

    public Course(String name, String description, Integer hours, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.hours = hours;
        this.price = price;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    // Вспомогательные методы
    public void addStudent(Student student) {
        students.add(student);
        student.getCourses().add(this);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.getCourses().remove(this);
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
        teacher.getCourses().add(this);
    }

    public void removeTeacher(Teacher teacher) {
        teachers.remove(teacher);
        teacher.getCourses().remove(this);
    }
}
