package com.example.project2.controller;

import com.example.project2.model.Student;
import com.example.project2.model.Group;
import com.example.project2.model.Course;
import com.example.project2.service.StudentService;
import com.example.project2.service.GroupService;
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

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private CourseService courseService;
    
    @GetMapping
    public String listStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long groupId,
            @RequestParam(required = false) Integer courseYear,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Student> students;
        
        if (search != null && !search.trim().isEmpty()) {
            students = studentService.findBySearchTerm(search.trim(), pageable);
        } else if (groupId != null) {
            List<Student> studentList = studentService.findByGroupId(groupId);
            students = Page.empty(pageable);
            // Для простоты, здесь можно реализовать пагинацию вручную
        } else if (courseYear != null) {
            List<Student> studentList = studentService.findByCourseYear(courseYear);
            students = Page.empty(pageable);
        } else if (startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<Student> studentList = studentService.findByBirthDateBetween(start, end);
            students = Page.empty(pageable);
        } else {
            students = studentService.findAll(pageable);
        }
        
        model.addAttribute("students", students);
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", students.getTotalPages());
        model.addAttribute("totalElements", students.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);
        model.addAttribute("groupId", groupId);
        model.addAttribute("courseYear", courseYear);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        return "students";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "createStudent";
    }
    
    @PostMapping("/new")
    public String createStudent(@Valid @ModelAttribute Student student, 
                               BindingResult result, 
                               @RequestParam(required = false) Long groupId,
                               @RequestParam(required = false) List<Long> courseIds,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("groups", groupService.findAll());
            model.addAttribute("courses", courseService.findAll());
            return "createStudent";
        }
        
        // Установка группы
        if (groupId != null) {
            groupService.findById(groupId).ifPresent(student::setGroup);
        }
        
        // Установка курсов
        if (courseIds != null) {
            courseIds.forEach(courseId -> 
                courseService.findById(courseId).ifPresent(course -> 
                    student.addCourse(course)));
        }
        
        studentService.save(student);
        redirectAttributes.addFlashAttribute("successMessage", "Студент успешно создан");
        return "redirect:/students";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        return studentService.findById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    model.addAttribute("groups", groupService.findAll());
                    model.addAttribute("courses", courseService.findAll());
                    return "editStudent";
                })
                .orElse("redirect:/students");
    }
    
    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable Long id,
                               @Valid @ModelAttribute Student student,
                               BindingResult result,
                               @RequestParam(required = false) Long groupId,
                               @RequestParam(required = false) List<Long> courseIds,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("groups", groupService.findAll());
            model.addAttribute("courses", courseService.findAll());
            return "editStudent";
        }
        
        // Установка группы
        if (groupId != null) {
            groupService.findById(groupId).ifPresent(student::setGroup);
        } else {
            student.setGroup(null);
        }
        
        // Очистка и установка курсов
        student.getCourses().clear();
        if (courseIds != null) {
            courseIds.forEach(courseId -> 
                courseService.findById(courseId).ifPresent(course -> 
                    student.addCourse(course)));
        }
        
        studentService.update(id, student);
        redirectAttributes.addFlashAttribute("successMessage", "Студент успешно обновлен");
        return "redirect:/students";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, 
                               @RequestParam(defaultValue = "soft") String deleteType,
                               RedirectAttributes redirectAttributes) {
        
        if ("hard".equals(deleteType)) {
            studentService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Студент полностью удален");
        } else {
            studentService.softDeleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Студент перемещен в корзину");
        }
        
        return "redirect:/students";
    }
    
    @PostMapping("/restore/{id}")
    public String restoreStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        studentService.restoreById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Студент восстановлен");
        return "redirect:/students";
    }
    
    @GetMapping("/view/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        return studentService.findById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    return "viewStudent";
                })
                .orElse("redirect:/students");
    }
}
