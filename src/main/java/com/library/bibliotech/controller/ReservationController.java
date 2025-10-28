package com.library.bibliotech.controller;

import com.library.bibliotech.model.ReaderProfile;
import com.library.bibliotech.model.Reservation;
import com.library.bibliotech.model.User;
import com.library.bibliotech.service.BookService;
import com.library.bibliotech.service.ReaderProfileService;
import com.library.bibliotech.service.ReservationService;
import com.library.bibliotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ReservationController {
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private ReaderProfileService readerProfileService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BookService bookService;
    
    @GetMapping("/reader/reservations")
    public String listMyReservations(Authentication auth, Model model) {
        String username = auth.getName();
        Optional<User> userOpt = userService.getUserByUsername(username);
        
        if (userOpt.isPresent()) {
            Optional<ReaderProfile> profileOpt = readerProfileService.getProfileByUserId(userOpt.get().getId());
            if (profileOpt.isPresent()) {
                List<Reservation> reservations = reservationService.getReservationsByReaderId(profileOpt.get().getId());
                model.addAttribute("reservations", reservations);
            }
        }
        
        return "reservations/my-list";
    }
    
    @PostMapping("/reader/reserve/{bookId}")
    public String createReservation(@PathVariable Long bookId, Authentication auth) {
        try {
            String username = auth.getName();
            Optional<User> userOpt = userService.getUserByUsername(username);
            
            if (userOpt.isPresent()) {
                Optional<ReaderProfile> profileOpt = readerProfileService.getProfileByUserId(userOpt.get().getId());
                if (profileOpt.isPresent()) {
                    reservationService.createReservation(profileOpt.get().getId(), bookId);
                    return "redirect:/reader/catalog?reserved";
                }
            }
            
            return "redirect:/reader/catalog?error";
        } catch (Exception e) {
            return "redirect:/reader/catalog?error=" + e.getMessage();
        }
    }
    
    @GetMapping("/librarian/reservations")
    public String listAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Reservation> reservationPage = reservationService.getAllReservations(pageable);
        
        model.addAttribute("reservations", reservationPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reservationPage.getTotalPages());
        model.addAttribute("totalItems", reservationPage.getTotalElements());
        return "reservations/list";
    }
    
    @PostMapping("/librarian/reservations/confirm/{id}")
    public String confirmPickup(@PathVariable Long id) {
        reservationService.confirmPickup(id);
        return "redirect:/librarian/reservations?confirmed";
    }
    
    @PostMapping("/librarian/reservations/cancel/{id}")
    public String cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return "redirect:/librarian/reservations?cancelled";
    }
}


