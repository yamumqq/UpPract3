package com.example.project2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название группы не может быть пустым")
    @Size(min = 2, max = 20, message = "Название группы должно содержать от 2 до 20 символов")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    @Column(name = "description")
    private String description;

    @Min(value = 1, message = "Курс должен быть не менее 1")
    @Max(value = 6, message = "Курс должен быть не более 6")
    @Column(name = "course_year", nullable = false)
    private Integer courseYear;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;


    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Student> students = new ArrayList<>();

    // Конструкторы
    public Group() {}

    public Group(String name, String description, Integer courseYear) {
        this.name = name;
        this.description = description;
        this.courseYear = courseYear;
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

    public Integer getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(Integer courseYear) {
        this.courseYear = courseYear;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    // Вспомогательные методы
    public void addStudent(Student student) {
        students.add(student);
        student.setGroup(this);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.setGroup(null);
    }
}
