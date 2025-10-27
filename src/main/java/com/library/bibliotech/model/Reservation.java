package com.library.bibliotech.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
public class Reservation {
    
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
    private LocalDate reservationDate;
    
    private LocalDate notificationDate;
    
    private LocalDate expiryDate;
    
    private String status = "PENDING";
    
    private boolean notificationSent = false;
    
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
    
    public LocalDate getReservationDate() {
        return reservationDate;
    }
    
    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }
    
    public LocalDate getNotificationDate() {
        return notificationDate;
    }
    
    public void setNotificationDate(LocalDate notificationDate) {
        this.notificationDate = notificationDate;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public boolean isNotificationSent() {
        return notificationSent;
    }
    
    public void setNotificationSent(boolean notificationSent) {
        this.notificationSent = notificationSent;
    }
}

