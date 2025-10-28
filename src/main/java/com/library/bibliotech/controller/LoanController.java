package com.library.bibliotech.controller;

import com.library.bibliotech.model.Loan;
import com.library.bibliotech.model.ReaderProfile;
import com.library.bibliotech.service.BookService;
import com.library.bibliotech.service.LoanService;
import com.library.bibliotech.service.ReaderProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/librarian/loans")
public class LoanController {
    
    @Autowired
    private LoanService loanService;
    
    @Autowired
    private ReaderProfileService readerProfileService;
    
    @Autowired
    private BookService bookService;
    
    @GetMapping
    public String listLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Loan> loanPage = loanService.getAllLoans(pageable);
        
        model.addAttribute("loans", loanPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", loanPage.getTotalPages());
        model.addAttribute("totalItems", loanPage.getTotalElements());
        return "loans/list";
    }
    
    @GetMapping("/active")
    public String listActiveLoans(Model model) {
        List<Loan> loans = loanService.getActiveLoans();
        model.addAttribute("loans", loans);
        return "loans/active";
    }
    
    @GetMapping("/overdue")
    public String listOverdueLoans(Model model) {
        List<Loan> loans = loanService.getOverdueLoans();
        model.addAttribute("loans", loans);
        return "loans/overdue";
    }
    
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("readers", readerProfileService.getAllProfiles());
        model.addAttribute("books", bookService.getAllBooks());
        return "loans/create";
    }
    
    @PostMapping("/create")
    public String createLoan(@RequestParam Long readerId, @RequestParam Long bookId, 
                            @RequestParam LocalDate dueDate, Model model) {
        try {
            loanService.createLoan(readerId, bookId, dueDate);
            return "redirect:/librarian/loans?success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("readers", readerProfileService.getAllProfiles());
            model.addAttribute("books", bookService.getAllBooks());
            return "loans/create";
        }
    }
    
    @PostMapping("/return/{id}")
    public String returnLoan(@PathVariable Long id) {
        try {
            loanService.returnLoan(id);
            return "redirect:/librarian/loans?returned";
        } catch (Exception e) {
            return "redirect:/librarian/loans?error=" + e.getMessage();
        }
    }
}


