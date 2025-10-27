package com.library.bibliotech.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "reader_profile_id", nullable = false)
    private ReaderProfile readerProfile;
    
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    
    @Column(nullable = false)
    private LocalDate loanDate;
    
    @Column(nullable = false)
    private LocalDate dueDate;
    
    private LocalDate returnDate;
    
    private String status = "ACTIVE";
    
    private boolean reminderSent = false;
    
    private boolean overdueSent = false;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public ReaderProfile getReaderProfile() {
        return readerProfile;
    }
    
    public void setReaderProfile(ReaderProfile readerProfile) {
        this.readerProfile = readerProfile;
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
    }
    
    public LocalDate getLoanDate() {
        return loanDate;
    }
    
    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public boolean isReminderSent() {
        return reminderSent;
    }
    
    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }
    
    public boolean isOverdueSent() {
        return overdueSent;
    }
    
    public void setOverdueSent(boolean overdueSent) {
        this.overdueSent = overdueSent;
    }
}

