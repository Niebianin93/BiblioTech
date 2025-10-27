package com.library.bibliotech.controller;

import com.library.bibliotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    
    @GetMapping("/activate")
    public String activateAccount(@RequestParam String token, Model model) {
        boolean success = userService.activateUser(token);
        if (success) {
            model.addAttribute("message", "Your account has been activated! You can now login.");
            model.addAttribute("success", true);
        } else {
            model.addAttribute("message", "Invalid or expired activation link.");
            model.addAttribute("success", false);
        }
        return "activation-result";
    }
}


