package com.example.project2.service;

import com.example.project2.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    
    // CRUD операции
    Address save(Address address);
    Optional<Address> findById(Long id);
    List<Address> findAll();
    Page<Address> findAll(Pageable pageable);
    Address update(Long id, Address address);
    void deleteById(Long id);
    void softDeleteById(Long id);
    
    // Поиск и фильтрация
    Page<Address> findBySearchTerm(String search, Pageable pageable);
    List<Address> findByCity(String city);
    List<Address> findByCountry(String country);
    List<Address> findByCityAndCountry(String city, String country);
    List<Address> findByPostalCode(String postalCode);
    Optional<Address> findByStudentId(Long studentId);
    
    // Статистика
    long count();
    
    // Восстановление
    void restoreById(Long id);
}
