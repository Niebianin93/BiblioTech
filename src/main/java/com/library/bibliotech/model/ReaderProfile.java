package com.library.bibliotech.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reader_profiles")
public class ReaderProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    private String firstName;
    
    private String lastName;
    
    private String phoneNumber;
    
    @Column(unique = true, nullable = false)
    private String libraryCardNumber;
    
    private int loanLimit = 5;
    
    @OneToMany(mappedBy = "readerProfile", cascade = CascadeType.ALL)
    private List<Loan> loans = new ArrayList<>();
    
    @OneToMany(mappedBy = "readerProfile", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getLibraryCardNumber() {
        return libraryCardNumber;
    }
    
    public void setLibraryCardNumber(String libraryCardNumber) {
        this.libraryCardNumber = libraryCardNumber;
    }
    
    public int getLoanLimit() {
        return loanLimit;
    }
    
    public void setLoanLimit(int loanLimit) {
        this.loanLimit = loanLimit;
    }
    
    public List<Loan> getLoans() {
        return loans;
    }
    
    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
    
    public List<Reservation> getReservations() {
        return reservations;
    }
    
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}

