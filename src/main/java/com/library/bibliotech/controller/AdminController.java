package com.library.bibliotech.controller;

import com.library.bibliotech.model.ReaderProfile;
import com.library.bibliotech.model.User;
import com.library.bibliotech.service.ReaderProfileService;
import com.library.bibliotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ReaderProfileService readerProfileService;
    
    @GetMapping("/users")
    public String listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> userPage = userService.getAllUsers(pageable);
        
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        return "admin/users";
    }
    
    @PostMapping("/users/enable/{id}")
    public String enableUser(@PathVariable Long id) {
        userService.enableUser(id);
        return "redirect:/admin/users?enabled";
    }
    
    @PostMapping("/users/add-role/{id}")
    public String addRole(@PathVariable Long id, @RequestParam String role) {
        if (role != null && !role.isEmpty()) {
            userService.setRole(id, role);
        }
        return "redirect:/admin/users?roleChanged";
    }
    
    @PostMapping("/users/remove-role/{id}")
    public String removeRole(@PathVariable Long id, @RequestParam String role) {
        userService.removeRole(id, role);
        return "redirect:/admin/users?roleRemoved";
    }
    
    @GetMapping("/readers")
    public String listReaders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<ReaderProfile> readerPage = readerProfileService.getAllProfiles(pageable);
        
        model.addAttribute("readers", readerPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", readerPage.getTotalPages());
        model.addAttribute("totalItems", readerPage.getTotalElements());
        return "admin/readers";
    }
    
    @PostMapping("/readers/update-limit/{id}")
    public String updateLoanLimit(@PathVariable Long id, @RequestParam int limit) {
        readerProfileService.updateLoanLimit(id, limit);
        return "redirect:/admin/readers?updated";
    }
    
    @GetMapping("/readers/edit/{id}")
    public String editReader(@PathVariable Long id, Model model) {
        ReaderProfile reader = readerProfileService.getProfileById(id).orElse(null);
        if (reader == null) {
            return "redirect:/admin/readers?error";
        }
        model.addAttribute("reader", reader);
        return "admin/reader-edit";
    }
    
    @PostMapping("/readers/edit/{id}")
    public String updateReader(@PathVariable Long id,
                              @RequestParam String firstName,
                              @RequestParam String lastName,
                              @RequestParam String phoneNumber,
                              @RequestParam String libraryCardNumber,
                              @RequestParam int loanLimit,
                              @RequestParam(defaultValue = "false") boolean enabled) {
        ReaderProfile profile = readerProfileService.getProfileById(id).orElse(null);
        if (profile != null) {
            profile.setFirstName(firstName);
            profile.setLastName(lastName);
            profile.setPhoneNumber(phoneNumber);
            profile.setLibraryCardNumber(libraryCardNumber);
            profile.setLoanLimit(loanLimit);
            
            User user = profile.getUser();
            user.setEnabled(enabled);
            userService.updateUser(user);
            
            readerProfileService.updateProfile(id, profile);
        }
        return "redirect:/admin/readers?updated";
    }
}


