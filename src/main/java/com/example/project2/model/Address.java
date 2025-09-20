package com.example.project2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "addresses")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Страна не может быть пустой")
    @Size(min = 2, max = 50, message = "Название страны должно содержать от 2 до 50 символов")
    @Column(name = "country", nullable = false)
    private String country;

    @NotBlank(message = "Город не может быть пустым")
    @Size(min = 2, max = 50, message = "Название города должно содержать от 2 до 50 символов")
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank(message = "Улица не может быть пустой")
    @Size(min = 2, max = 100, message = "Название улицы должно содержать от 2 до 100 символов")
    @Column(name = "street", nullable = false)
    private String street;

    @NotBlank(message = "Номер дома не может быть пустым")
    @Size(max = 10, message = "Номер дома не должен превышать 10 символов")
    @Column(name = "house_number", nullable = false)
    private String houseNumber;

    @Size(max = 10, message = "Номер квартиры не должен превышать 10 символов")
    @Column(name = "apartment")
    private String apartment;

    @Pattern(regexp = "^\\d{6}$", message = "Почтовый индекс должен содержать 6 цифр")
    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    // Связь 1:1 с Student
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    // Конструкторы
    public Address() {}

    public Address(String country, String city, String street, String houseNumber, 
                   String apartment, String postalCode) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartment = apartment;
        this.postalCode = postalCode;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    // Вспомогательные методы
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        address.append(country).append(", ");
        address.append(city).append(", ");
        address.append(street).append(", ");
        address.append(houseNumber);
        if (apartment != null && !apartment.isEmpty()) {
            address.append(", кв. ").append(apartment);
        }
        if (postalCode != null && !postalCode.isEmpty()) {
            address.append(", ").append(postalCode);
        }
        return address.toString();
    }
}
