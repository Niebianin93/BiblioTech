package com.library.bibliotech.repository;

import com.library.bibliotech.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByReaderProfileId(Long readerProfileId);
    List<Loan> findByBookId(Long bookId);
    List<Loan> findByStatus(String status);
    List<Loan> findByReaderProfileIdAndStatus(Long readerProfileId, String status);
    List<Loan> findByBookIdAndStatus(Long bookId, String status);
    List<Loan> findByDueDateBeforeAndStatus(LocalDate date, String status);
    long countByReaderProfileIdAndStatus(Long readerProfileId, String status);
}

