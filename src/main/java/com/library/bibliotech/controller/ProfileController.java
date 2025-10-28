package com.library.bibliotech.controller;

import com.library.bibliotech.model.Loan;
import com.library.bibliotech.model.ReaderProfile;
import com.library.bibliotech.model.User;
import com.library.bibliotech.service.LoanService;
import com.library.bibliotech.service.ReaderProfileService;
import com.library.bibliotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reader/profile")
public class ProfileController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ReaderProfileService readerProfileService;
    
    @Autowired
    private LoanService loanService;
    
    @GetMapping
    public String viewProfile(Authentication auth, Model model) {
        String username = auth.getName();
        Optional<User> userOpt = userService.getUserByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("user", user);
            
            Optional<ReaderProfile> profileOpt = readerProfileService.getProfileByUserId(user.getId());
            if (profileOpt.isPresent()) {
                model.addAttribute("profile", profileOpt.get());
            }
        }
        
        return "reader/profile";
    }
    
    @GetMapping("/loans")
    public String viewMyLoans(Authentication auth, Model model) {
        String username = auth.getName();
        Optional<User> userOpt = userService.getUserByUsername(username);
        
        if (userOpt.isPresent()) {
            Optional<ReaderProfile> profileOpt = readerProfileService.getProfileByUserId(userOpt.get().getId());
            if (profileOpt.isPresent()) {
                List<Loan> loans = loanService.getLoansByReaderId(profileOpt.get().getId());
                model.addAttribute("loans", loans);
            }
        }
        
        return "reader/my-loans";
    }
    
    @PostMapping("/update")
    public String updateProfile(
            Authentication auth,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String phoneNumber,
            Model model) {
        
        try {
            String username = auth.getName();
            System.out.println("UPDATE PROFILE: Username = " + username);
            Optional<User> userOpt = userService.getUserByUsername(username);
            
            if (userOpt.isPresent()) {
                System.out.println("UPDATE PROFILE: User found, ID = " + userOpt.get().getId());
                Optional<ReaderProfile> profileOpt = readerProfileService.getProfileByUserId(userOpt.get().getId());
                if (profileOpt.isPresent()) {
                    System.out.println("UPDATE PROFILE: Profile found, updating...");
                    ReaderProfile profile = profileOpt.get();
                    profile.setFirstName(firstName);
                    profile.setLastName(lastName);
                    profile.setPhoneNumber(phoneNumber);
                    readerProfileService.updateProfile(profile.getId(), profile);
                    System.out.println("UPDATE PROFILE: Success!");
                    return "redirect:/reader/profile?updated";
                } else {
                    System.out.println("UPDATE PROFILE: Profile NOT found for user ID " + userOpt.get().getId());
                }
            } else {
                System.out.println("UPDATE PROFILE: User NOT found");
            }
            
            return "redirect:/reader/profile?error";
        } catch (Exception e) {
            System.out.println("UPDATE PROFILE ERROR: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/reader/profile?error=" + e.getMessage();
        }
    }
}


