package com.example.project2.controller;

import com.example.project2.model.Role;
import com.example.project2.model.User;
import com.example.project2.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Неверное имя пользователя или пароль");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Вы успешно вышли из системы");
        }
        return "login";
    }
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid User user, 
                              BindingResult bindingResult,
                              @RequestParam("confirmPassword") String confirmPassword,
                              RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "register";
        }
        
        if (!user.getPassword().equals(confirmPassword)) {
            bindingResult.rejectValue("password", "error.user", "Пароли не совпадают");
            return "register";
        }
        
        try {
            userService.registerUser(user.getUsername(), user.getEmail(), user.getPassword(), Role.USER);
            redirectAttributes.addFlashAttribute("successMessage", "Регистрация прошла успешно! Теперь вы можете войти в систему.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            bindingResult.rejectValue("username", "error.user", e.getMessage());
            return "register";
        }
    }
    
    @GetMapping("/profile")
    public String profilePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        
        model.addAttribute("user", user);
        return "profile";
    }
}

