package com.example.project2.service;

import com.example.project2.model.Address;
import com.example.project2.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {
    
    @Autowired
    private AddressRepository addressRepository;
    
    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Address> findById(Long id) {
        return addressRepository.findById(id)
                .filter(address -> !address.getIsDeleted());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Address> findAll() {
        return addressRepository.findByIsDeletedFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Address> findAll(Pageable pageable) {
        return addressRepository.findByIsDeletedFalse(pageable);
    }
    
    @Override
    public Address update(Long id, Address address) {
        return addressRepository.findById(id)
                .map(existingAddress -> {
                    if (existingAddress.getIsDeleted()) {
                        throw new RuntimeException("Адрес удален и не может быть изменен");
                    }
                    existingAddress.setCountry(address.getCountry());
                    existingAddress.setCity(address.getCity());
                    existingAddress.setStreet(address.getStreet());
                    existingAddress.setHouseNumber(address.getHouseNumber());
                    existingAddress.setApartment(address.getApartment());
                    existingAddress.setPostalCode(address.getPostalCode());
                    return addressRepository.save(existingAddress);
                })
                .orElseThrow(() -> new RuntimeException("Адрес не найден"));
    }
    
    @Override
    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }
    
    @Override
    public void softDeleteById(Long id) {
        addressRepository.findById(id)
                .ifPresent(address -> {
                    address.setIsDeleted(true);
                    addressRepository.save(address);
                });
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Address> findBySearchTerm(String search, Pageable pageable) {
        return addressRepository.findBySearchTerm(search, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Address> findByCity(String city) {
        return addressRepository.findByCityAndIsDeletedFalse(city);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Address> findByCountry(String country) {
        return addressRepository.findByCountryAndIsDeletedFalse(country);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Address> findByCityAndCountry(String city, String country) {
        return addressRepository.findByCityAndCountryAndIsDeletedFalse(city, country);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Address> findByPostalCode(String postalCode) {
        return addressRepository.findByPostalCodeAndIsDeletedFalse(postalCode);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Address> findByStudentId(Long studentId) {
        return addressRepository.findByStudentIdAndIsDeletedFalse(studentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long count() {
        return addressRepository.countByIsDeletedFalse();
    }
    
    @Override
    public void restoreById(Long id) {
        addressRepository.findById(id)
                .ifPresent(address -> {
                    address.setIsDeleted(false);
                    addressRepository.save(address);
                });
    }
}
