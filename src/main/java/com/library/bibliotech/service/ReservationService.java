package com.library.bibliotech.service;

import com.library.bibliotech.model.Book;
import com.library.bibliotech.model.ReaderProfile;
import com.library.bibliotech.model.Reservation;
import com.library.bibliotech.repository.BookRepository;
import com.library.bibliotech.repository.ReaderProfileRepository;
import com.library.bibliotech.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private ReaderProfileRepository readerProfileRepository;
    
    @Transactional
    public Reservation createReservation(Long readerProfileId, Long bookId) {
        Optional<ReaderProfile> profileOpt = readerProfileRepository.findById(readerProfileId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        
        if (profileOpt.isEmpty() || bookOpt.isEmpty()) {
            throw new RuntimeException("Reader or Book not found");
        }
        
        ReaderProfile profile = profileOpt.get();
        Book book = bookOpt.get();
        
        Reservation reservation = new Reservation();
        reservation.setReaderProfile(profile);
        reservation.setBook(book);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus("PENDING");
        
        return reservationRepository.save(reservation);
    }
    
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    
    public Page<Reservation> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }
    
    public List<Reservation> getReservationsByReaderId(Long readerId) {
        return reservationRepository.findByReaderProfileId(readerId);
    }
    
    public List<Reservation> getPendingReservations() {
        return reservationRepository.findByStatus("PENDING");
    }
    
    @Transactional
    public void confirmPickup(Long reservationId) {
        Optional<Reservation> resOpt = reservationRepository.findById(reservationId);
        if (resOpt.isPresent()) {
            Reservation reservation = resOpt.get();
            reservation.setStatus("COMPLETED");
            reservationRepository.save(reservation);
        }
    }
    
    @Transactional
    public void cancelReservation(Long reservationId) {
        Optional<Reservation> resOpt = reservationRepository.findById(reservationId);
        if (resOpt.isPresent()) {
            Reservation reservation = resOpt.get();
            reservation.setStatus("CANCELLED");
            reservationRepository.save(reservation);
        }
    }
}


