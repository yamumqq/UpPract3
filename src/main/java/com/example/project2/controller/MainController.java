package com.example.project2.controller;

import com.example.project2.service.StudentService;
import com.example.project2.service.GroupService;
import com.example.project2.service.CourseService;
import com.example.project2.service.AddressService;
import com.example.project2.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private AddressService addressService;
    
    @Autowired
    private TeacherService teacherService;
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("studentCount", studentService.count());
        model.addAttribute("groupCount", groupService.count());
        model.addAttribute("courseCount", courseService.count());
        model.addAttribute("addressCount", addressService.count());
        model.addAttribute("teacherCount", teacherService.count());
        return "index";
    }
}
