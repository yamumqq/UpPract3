package com.example.project2.controller;

import com.example.project2.model.Teacher;
import com.example.project2.service.TeacherService;
import com.example.project2.service.CourseService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private CourseService courseService;
    
    @GetMapping
    public String listTeachers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long courseId,
            Model model) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Teacher> teachers;
        
        if (search != null && !search.trim().isEmpty()) {
            teachers = teacherService.findBySearchTerm(search.trim(), pageable);
        } else if (specialization != null) {
            List<Teacher> teacherList = teacherService.findBySpecialization(specialization);
            teachers = Page.empty(pageable);
        } else if (minExperience != null) {
            List<Teacher> teacherList = teacherService.findByMinExperience(minExperience);
            teachers = Page.empty(pageable);
        } else if (minSalary != null && maxSalary != null) {
            teachers = teacherService.findBySalaryBetween(minSalary, maxSalary, pageable);
        } else if (startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<Teacher> teacherList = teacherService.findByHireDateBetween(start, end);
            teachers = Page.empty(pageable);
        } else if (courseId != null) {
            List<Teacher> teacherList = teacherService.findByCourseId(courseId);
            teachers = Page.empty(pageable);
        } else {
            teachers = teacherService.findAll(pageable);
        }
        
        model.addAttribute("teachers", teachers);
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", teachers.getTotalPages());
        model.addAttribute("totalElements", teachers.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);
        model.addAttribute("specialization", specialization);
        model.addAttribute("minExperience", minExperience);
        model.addAttribute("minSalary", minSalary);
        model.addAttribute("maxSalary", maxSalary);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("courseId", courseId);
        
        return "teachers";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("courses", courseService.findAll());
        return "createTeacher";
    }
    
    @PostMapping("/new")
    public String createTeacher(@Valid @ModelAttribute Teacher teacher, 
                               BindingResult result,
                               @RequestParam(required = false) List<Long> courseIds,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("courses", courseService.findAll());
            return "createTeacher";
        }
        
        // Установка курсов
        if (courseIds != null) {
            courseIds.forEach(courseId -> 
                courseService.findById(courseId).ifPresent(course -> 
                    teacher.addCourse(course)));
        }
        
        teacherService.save(teacher);
        redirectAttributes.addFlashAttribute("successMessage", "Преподаватель успешно создан");
        return "redirect:/teachers";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        return teacherService.findById(id)
                .map(teacher -> {
                    model.addAttribute("teacher", teacher);
                    model.addAttribute("courses", courseService.findAll());
                    return "editTeacher";
                })
                .orElse("redirect:/teachers");
    }
    
    @PostMapping("/edit/{id}")
    public String updateTeacher(@PathVariable Long id,
                               @Valid @ModelAttribute Teacher teacher,
                               BindingResult result,
                               @RequestParam(required = false) List<Long> courseIds,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("courses", courseService.findAll());
            return "editTeacher";
        }
        
        // Очистка и установка курсов
        teacher.getCourses().clear();
        if (courseIds != null) {
            courseIds.forEach(courseId -> 
                courseService.findById(courseId).ifPresent(course -> 
                    teacher.addCourse(course)));
        }
        
        teacherService.update(id, teacher);
        redirectAttributes.addFlashAttribute("successMessage", "Преподаватель успешно обновлен");
        return "redirect:/teachers";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable Long id, 
                               @RequestParam(defaultValue = "soft") String deleteType,
                               RedirectAttributes redirectAttributes) {
        
        if ("hard".equals(deleteType)) {
            teacherService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Преподаватель полностью удален");
        } else {
            teacherService.softDeleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Преподаватель перемещен в корзину");
        }
        
        return "redirect:/teachers";
    }
    
    @PostMapping("/restore/{id}")
    public String restoreTeacher(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        teacherService.restoreById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Преподаватель восстановлен");
        return "redirect:/teachers";
    }
    
    @GetMapping("/view/{id}")
    public String viewTeacher(@PathVariable Long id, Model model) {
        return teacherService.findById(id)
                .map(teacher -> {
                    model.addAttribute("teacher", teacher);
                    return "viewTeacher";
                })
                .orElse("redirect:/teachers");
    }
}
