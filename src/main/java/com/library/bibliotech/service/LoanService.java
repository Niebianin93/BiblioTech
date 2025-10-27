package com.library.bibliotech.service;

import com.library.bibliotech.model.Book;
import com.library.bibliotech.model.Loan;
import com.library.bibliotech.model.ReaderProfile;
import com.library.bibliotech.model.Reservation;
import com.library.bibliotech.repository.BookRepository;
import com.library.bibliotech.repository.LoanRepository;
import com.library.bibliotech.repository.ReaderProfileRepository;
import com.library.bibliotech.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    
    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private ReaderProfileRepository readerProfileRepository;
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Value("${library.loan.default-days:14}")
    private int defaultLoanDays;
    
    @Transactional
    public Loan createLoan(Long readerProfileId, Long bookId) {
        return createLoan(readerProfileId, bookId, LocalDate.now().plusDays(defaultLoanDays));
    }
    
    @Transactional
    public Loan createLoan(Long readerProfileId, Long bookId, LocalDate dueDate) {
        Optional<ReaderProfile> profileOpt = readerProfileRepository.findById(readerProfileId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        
        if (profileOpt.isEmpty() || bookOpt.isEmpty()) {
            throw new RuntimeException("Reader or Book not found");
        }
        
        ReaderProfile profile = profileOpt.get();
        Book book = bookOpt.get();
        
        long activeLoanCount = loanRepository.countByReaderProfileIdAndStatus(readerProfileId, "ACTIVE");
        if (activeLoanCount >= profile.getLoanLimit()) {
            throw new RuntimeException("Loan limit exceeded");
        }
        
        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No available copies");
        }
        
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        
        Loan loan = new Loan();
        loan.setReaderProfile(profile);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(dueDate);
        loan.setStatus("ACTIVE");
        
        return loanRepository.save(loan);
    }
    
    @Transactional
    public Loan returnLoan(Long loanId) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isEmpty()) {
            throw new RuntimeException("Loan not found");
        }
        
        Loan loan = loanOpt.get();
        loan.setReturnDate(LocalDate.now());
        loan.setStatus("RETURNED");
        loanRepository.save(loan);
        
        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
        
        List<Reservation> pendingReservations = reservationRepository
                .findByBookIdAndStatusOrderByReservationDateAsc(book.getId(), "PENDING");
        
        if (!pendingReservations.isEmpty()) {
            Reservation firstReservation = pendingReservations.get(0);
            firstReservation.setStatus("AVAILABLE");
            firstReservation.setNotificationDate(LocalDate.now());
            firstReservation.setExpiryDate(LocalDate.now().plusDays(3));
            reservationRepository.save(firstReservation);
            
            String email = firstReservation.getReaderProfile().getUser().getEmail();
            emailService.sendReservationAvailable(email, book.getTitle());
        }
        
        return loan;
    }
    
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
    
    public Page<Loan> getAllLoans(Pageable pageable) {
        return loanRepository.findAll(pageable);
    }
    
    public List<Loan> getLoansByReaderId(Long readerId) {
        return loanRepository.findByReaderProfileId(readerId);
    }
    
    public List<Loan> getActiveLoans() {
        return loanRepository.findByStatus("ACTIVE");
    }
    
    public List<Loan> getOverdueLoans() {
        return loanRepository.findByDueDateBeforeAndStatus(LocalDate.now(), "ACTIVE");
    }
}


