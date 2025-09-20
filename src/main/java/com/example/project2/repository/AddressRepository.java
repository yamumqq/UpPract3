package com.example.project2.repository;

import com.example.project2.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    

    List<Address> findByIsDeletedFalse();
    

    Page<Address> findByIsDeletedFalse(Pageable pageable);
    

    List<Address> findByCityAndIsDeletedFalse(String city);
    

    List<Address> findByCountryAndIsDeletedFalse(String country);
    

    List<Address> findByCityAndCountryAndIsDeletedFalse(String city, String country);
    

    List<Address> findByPostalCodeAndIsDeletedFalse(String postalCode);
    

    @Query("SELECT a FROM Address a WHERE a.student.id = :studentId AND a.isDeleted = false")
    Optional<Address> findByStudentIdAndIsDeletedFalse(@Param("studentId") Long studentId);
    
    // Поиск по адресным данным
    @Query("SELECT a FROM Address a WHERE a.isDeleted = false AND " +
           "(LOWER(a.city) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.country) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.street) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Address> findBySearchTerm(@Param("search") String search, Pageable pageable);
    
    // Подсчет активных адресов
    long countByIsDeletedFalse();
}
