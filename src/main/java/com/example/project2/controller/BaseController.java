package com.example.project2.controller;

import com.example.project2.model.User;
import com.example.project2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

public abstract class BaseController {
    
    @Autowired
    protected UserService userService;
    
    protected void addUserInfoToModel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            User currentUser = userService.findByUsername(auth.getName()).orElse(null);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("isAdmin", auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));
        } else {
            model.addAttribute("isAuthenticated", false);
            model.addAttribute("isAdmin", false);
        }
    }
}
