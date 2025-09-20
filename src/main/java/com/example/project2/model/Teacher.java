package com.example.project2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teachers")
public class Teacher {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = 2, max = 50, message = "Фамилия должна содержать от 2 до 50 символов")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email не может быть пустым")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^\\+7\\d{10}$", message = "Телефон должен быть в формате +7XXXXXXXXXX")
    @Column(name = "phone")
    private String phone;

    @NotBlank(message = "Специализация не может быть пустой")
    @Size(max = 100, message = "Специализация не должна превышать 100 символов")
    @Column(name = "specialization", nullable = false)
    private String specialization;

    @Min(value = 0, message = "Опыт работы не может быть отрицательным")
    @Max(value = 50, message = "Опыт работы не может превышать 50 лет")
    @Column(name = "experience_years")
    private Integer experienceYears;

    @NotNull(message = "Дата найма обязательна")
    @PastOrPresent(message = "Дата найма не может быть в будущем")
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @DecimalMin(value = "0.0", message = "Зарплата не может быть отрицательной")
    @Column(name = "salary", precision = 10, scale = 2)
    private java.math.BigDecimal salary;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    // Связь M:M с Course
    @ManyToMany(mappedBy = "teachers", fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();

    // Конструкторы
    public Teacher() {}

    public Teacher(String firstName, String lastName, String email, String phone, 
                   String specialization, Integer experienceYears, LocalDate hireDate, 
                   java.math.BigDecimal salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.hireDate = hireDate;
        this.salary = salary;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public java.math.BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(java.math.BigDecimal salary) {
        this.salary = salary;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    // Вспомогательные методы
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addCourse(Course course) {
        courses.add(course);
        course.getTeachers().add(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.getTeachers().remove(this);
    }
}
