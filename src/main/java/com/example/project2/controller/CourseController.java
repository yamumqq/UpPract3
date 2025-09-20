package com.example.project2.controller;

import com.example.project2.model.Course;
import com.example.project2.service.CourseService;
import com.example.project2.service.StudentService;
import com.example.project2.service.TeacherService;
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
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController extends BaseController {
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherService teacherService;
    
    @GetMapping
    public String listCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer minHours,
            @RequestParam(required = false) Integer maxHours,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long teacherId,
            Model model) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Course> courses;
        
        if (search != null && !search.trim().isEmpty()) {
            courses = courseService.findBySearchTerm(search.trim(), pageable);
        } else if (minHours != null && maxHours != null) {
            courses = courseService.findByHoursBetween(minHours, maxHours, pageable);
        } else if (minPrice != null && maxPrice != null) {
            courses = courseService.findByPriceBetween(minPrice, maxPrice, pageable);
        } else if (teacherId != null) {
            List<Course> courseList = courseService.findByTeacherId(teacherId);
            courses = Page.empty(pageable);
        } else {
            courses = courseService.findAll(pageable);
        }
        
        model.addAttribute("courses", courses);
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", courses.getTotalPages());
        model.addAttribute("totalElements", courses.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);
        model.addAttribute("minHours", minHours);
        model.addAttribute("maxHours", maxHours);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("teacherId", teacherId);
        
        // Добавляем информацию о текущем пользователе
        addUserInfoToModel(model);
        
        return "courses";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        return "createCourse";
    }
    
    @PostMapping("/new")
    public String createCourse(@Valid @ModelAttribute Course course, 
                              BindingResult result,
                              @RequestParam(required = false) List<Long> studentIds,
                              @RequestParam(required = false) List<Long> teacherIds,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("students", studentService.findAll());
            model.addAttribute("teachers", teacherService.findAll());
            return "createCourse";
        }
        
        // Установка студентов
        if (studentIds != null) {
            studentIds.forEach(studentId -> 
                studentService.findById(studentId).ifPresent(student -> 
                    course.addStudent(student)));
        }
        
        // Установка преподавателей
        if (teacherIds != null) {
            teacherIds.forEach(teacherId -> 
                teacherService.findById(teacherId).ifPresent(teacher -> 
                    course.addTeacher(teacher)));
        }
        
        courseService.save(course);
        redirectAttributes.addFlashAttribute("successMessage", "Курс успешно создан");
        return "redirect:/courses";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        return courseService.findById(id)
                .map(course -> {
                    model.addAttribute("course", course);
                    model.addAttribute("students", studentService.findAll());
                    model.addAttribute("teachers", teacherService.findAll());
                    return "editCourse";
                })
                .orElse("redirect:/courses");
    }
    
    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable Long id,
                              @Valid @ModelAttribute Course course,
                              BindingResult result,
                              @RequestParam(required = false) List<Long> studentIds,
                              @RequestParam(required = false) List<Long> teacherIds,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("students", studentService.findAll());
            model.addAttribute("teachers", teacherService.findAll());
            return "editCourse";
        }
        
        // Очистка и установка студентов
        course.getStudents().clear();
        if (studentIds != null) {
            studentIds.forEach(studentId -> 
                studentService.findById(studentId).ifPresent(student -> 
                    course.addStudent(student)));
        }
        
        // Очистка и установка преподавателей
        course.getTeachers().clear();
        if (teacherIds != null) {
            teacherIds.forEach(teacherId -> 
                teacherService.findById(teacherId).ifPresent(teacher -> 
                    course.addTeacher(teacher)));
        }
        
        courseService.update(id, course);
        redirectAttributes.addFlashAttribute("successMessage", "Курс успешно обновлен");
        return "redirect:/courses";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, 
                              @RequestParam(defaultValue = "soft") String deleteType,
                              RedirectAttributes redirectAttributes) {
        
        if ("hard".equals(deleteType)) {
            courseService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Курс полностью удален");
        } else {
            courseService.softDeleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Курс перемещен в корзину");
        }
        
        return "redirect:/courses";
    }
    
    @PostMapping("/restore/{id}")
    public String restoreCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        courseService.restoreById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Курс восстановлен");
        return "redirect:/courses";
    }
    
    @GetMapping("/view/{id}")
    public String viewCourse(@PathVariable Long id, Model model) {
        return courseService.findById(id)
                .map(course -> {
                    model.addAttribute("course", course);
                    return "viewCourse";
                })
                .orElse("redirect:/courses");
    }
}
