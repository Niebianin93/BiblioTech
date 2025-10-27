package com.library.bibliotech.service;

import com.library.bibliotech.model.Book;
import com.library.bibliotech.model.Loan;
import com.library.bibliotech.model.Reservation;
import com.library.bibliotech.repository.BookRepository;
import com.library.bibliotech.repository.LoanRepository;
import com.library.bibliotech.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }
    
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
    
    public Page<Book> searchByTitle(String title, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
    }
    
    public Page<Book> searchByAuthor(String author, Pageable pageable) {
        return bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
    }
    
    public Book updateBook(Long id, Book bookDetails) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setIsbn(bookDetails.getIsbn());
            book.setPublisher(bookDetails.getPublisher());
            book.setPublicationYear(bookDetails.getPublicationYear());
            book.setTotalCopies(bookDetails.getTotalCopies());
            book.setAvailableCopies(bookDetails.getAvailableCopies());
            return bookRepository.save(book);
        }
        return null;
    }
    
    @Transactional
    public void deleteBook(Long id) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            List<Loan> activeLoans = loanRepository.findByBookIdAndStatus(id, "ACTIVE");
            List<Reservation> activeReservations = reservationRepository.findByBookIdAndStatusOrderByReservationDateAsc(id, "PENDING");
            
            if (!activeLoans.isEmpty() || !activeReservations.isEmpty()) {
                throw new RuntimeException("Cannot delete book with active loans or reservations");
            }
            
            bookRepository.deleteById(id);
        }
    }
    
    public boolean isAvailable(Long bookId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        return bookOpt.isPresent() && bookOpt.get().getAvailableCopies() > 0;
    }
}

