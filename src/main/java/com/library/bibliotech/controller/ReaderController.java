package com.library.bibliotech.controller;

import com.library.bibliotech.model.Book;
import com.library.bibliotech.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reader")
public class ReaderController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping("/catalog")
    public String viewCatalog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage;
        
        if (search != null && !search.isEmpty()) {
            bookPage = bookService.searchByTitle(search, pageable);
        } else {
            bookPage = bookService.getAllBooks(pageable);
        }
        
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        
        return "reader/catalog";
    }
}


