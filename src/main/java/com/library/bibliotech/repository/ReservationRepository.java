package com.library.bibliotech.repository;

import com.library.bibliotech.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByReaderProfileId(Long readerProfileId);
    List<Reservation> findByBookId(Long bookId);
    List<Reservation> findByStatus(String status);
    List<Reservation> findByBookIdAndStatusOrderByReservationDateAsc(Long bookId, String status);
    List<Reservation> findByExpiryDateBeforeAndStatus(LocalDate date, String status);
}


