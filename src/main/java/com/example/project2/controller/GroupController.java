package com.example.project2.controller;

import com.example.project2.model.Group;
import com.example.project2.service.GroupService;
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
@RequestMapping("/groups")
public class GroupController {
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    public String listGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer courseYear,
            Model model) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Group> groups;
        
        if (search != null && !search.trim().isEmpty()) {
            groups = groupService.findBySearchTerm(search.trim(), pageable);
        } else if (courseYear != null) {
            groups = groupService.findByCourseYear(courseYear, pageable);
        } else {
            groups = groupService.findAll(pageable);
        }
        
        model.addAttribute("groups", groups);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", groups.getTotalPages());
        model.addAttribute("totalElements", groups.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);
        model.addAttribute("courseYear", courseYear);
        
        return "groups";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("group", new Group());
        return "createGroup";
    }
    
    @PostMapping("/new")
    public String createGroup(@Valid @ModelAttribute Group group, 
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "createGroup";
        }
        
        groupService.save(group);
        redirectAttributes.addFlashAttribute("successMessage", "Группа успешно создана");
        return "redirect:/groups";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        return groupService.findById(id)
                .map(group -> {
                    model.addAttribute("group", group);
                    return "editGroup";
                })
                .orElse("redirect:/groups");
    }
    
    @PostMapping("/edit/{id}")
    public String updateGroup(@PathVariable Long id,
                             @Valid @ModelAttribute Group group,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "editGroup";
        }
        
        groupService.update(id, group);
        redirectAttributes.addFlashAttribute("successMessage", "Группа успешно обновлена");
        return "redirect:/groups";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteGroup(@PathVariable Long id, 
                             @RequestParam(defaultValue = "soft") String deleteType,
                             RedirectAttributes redirectAttributes) {
        
        if ("hard".equals(deleteType)) {
            groupService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Группа полностью удалена");
        } else {
            groupService.softDeleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Группа перемещена в корзину");
        }
        
        return "redirect:/groups";
    }
    
    @PostMapping("/restore/{id}")
    public String restoreGroup(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        groupService.restoreById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Группа восстановлена");
        return "redirect:/groups";
    }
    
    @GetMapping("/view/{id}")
    public String viewGroup(@PathVariable Long id, Model model) {
        return groupService.findById(id)
                .map(group -> {
                    model.addAttribute("group", group);
                    model.addAttribute("students", studentService.findByGroupId(id));
                    return "viewGroup";
                })
                .orElse("redirect:/groups");
    }
}
