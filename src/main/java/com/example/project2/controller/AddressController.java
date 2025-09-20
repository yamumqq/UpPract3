package com.example.project2.controller;

import com.example.project2.model.Address;
import com.example.project2.service.AddressService;
import com.example.project2.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/addresses")
public class AddressController {
    
    @Autowired
    private AddressService addressService;
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    public String listAddresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String postalCode,
            Model model) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Address> addresses;
        
        if (search != null && !search.trim().isEmpty()) {
            addresses = addressService.findBySearchTerm(search.trim(), pageable);
        } else if (city != null && country != null) {
            List<Address> addressList = addressService.findByCityAndCountry(city, country);
            addresses = Page.empty(pageable);
        } else if (city != null) {
            List<Address> addressList = addressService.findByCity(city);
            addresses = Page.empty(pageable);
        } else if (country != null) {
            List<Address> addressList = addressService.findByCountry(country);
            addresses = Page.empty(pageable);
        } else if (postalCode != null) {
            List<Address> addressList = addressService.findByPostalCode(postalCode);
            addresses = Page.empty(pageable);
        } else {
            addresses = addressService.findAll(pageable);
        }
        
        model.addAttribute("addresses", addresses);
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", addresses.getTotalPages());
        model.addAttribute("totalElements", addresses.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);
        model.addAttribute("city", city);
        model.addAttribute("country", country);
        model.addAttribute("postalCode", postalCode);
        
        return "addresses";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("address", new Address());
        model.addAttribute("students", studentService.findAll());
        return "createAddress";
    }
    
    @PostMapping("/new")
    public String createAddress(@Valid @ModelAttribute Address address, 
                               BindingResult result,
                               @RequestParam(required = false) Long studentId,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("students", studentService.findAll());
            return "createAddress";
        }
        
        // Установка студента
        if (studentId != null) {
            studentService.findById(studentId).ifPresent(student -> {
                address.setStudent(student);
                student.setAddress(address);
            });
        }
        
        addressService.save(address);
        redirectAttributes.addFlashAttribute("successMessage", "Адрес успешно создан");
        return "redirect:/addresses";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        return addressService.findById(id)
                .map(address -> {
                    model.addAttribute("address", address);
                    model.addAttribute("students", studentService.findAll());
                    return "editAddress";
                })
                .orElse("redirect:/addresses");
    }
    
    @PostMapping("/edit/{id}")
    public String updateAddress(@PathVariable Long id,
                               @Valid @ModelAttribute Address address,
                               BindingResult result,
                               @RequestParam(required = false) Long studentId,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("students", studentService.findAll());
            return "editAddress";
        }
        
        // Установка студента
        if (studentId != null) {
            studentService.findById(studentId).ifPresent(student -> {
                address.setStudent(student);
                student.setAddress(address);
            });
        } else {
            address.setStudent(null);
        }
        
        addressService.update(id, address);
        redirectAttributes.addFlashAttribute("successMessage", "Адрес успешно обновлен");
        return "redirect:/addresses";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Long id, 
                               @RequestParam(defaultValue = "soft") String deleteType,
                               RedirectAttributes redirectAttributes) {
        
        if ("hard".equals(deleteType)) {
            addressService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Адрес полностью удален");
        } else {
            addressService.softDeleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Адрес перемещен в корзину");
        }
        
        return "redirect:/addresses";
    }
    
    @PostMapping("/restore/{id}")
    public String restoreAddress(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        addressService.restoreById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Адрес восстановлен");
        return "redirect:/addresses";
    }
    
    @GetMapping("/view/{id}")
    public String viewAddress(@PathVariable Long id, Model model) {
        return addressService.findById(id)
                .map(address -> {
                    model.addAttribute("address", address);
                    return "viewAddress";
                })
                .orElse("redirect:/addresses");
    }
}
