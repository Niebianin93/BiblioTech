package com.library.bibliotech.controller;

import com.library.bibliotech.model.Book;
import com.library.bibliotech.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/librarian/books")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String searchType,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Book> bookPage;
        
        if (search != null && !search.isEmpty()) {
            if ("author".equals(searchType)) {
                bookPage = bookService.searchByAuthor(search, pageable);
            } else {
                bookPage = bookService.searchByTitle(search, pageable);
            }
        } else {
            bookPage = bookService.getAllBooks(pageable);
        }
        
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalItems", bookPage.getTotalElements());
        
        return "books/list";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "books/add";
    }
    
    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book, Model model) {
        try {
            bookService.addBook(book);
            return "redirect:/librarian/books?success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "books/add";
        }
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        model.addAttribute("book", book);
        return "books/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book, Model model) {
        try {
            bookService.updateBook(id, book);
            return "redirect:/librarian/books?updated";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "books/edit";
        }
    }
    
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, Model model) {
        try {
            bookService.deleteBook(id);
            return "redirect:/librarian/books?deleted";
        } catch (Exception e) {
            return "redirect:/librarian/books?error=" + e.getMessage();
        }
    }
}


