package com.library.bibliotech.repository;

import com.library.bibliotech.model.ReaderProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReaderProfileRepository extends JpaRepository<ReaderProfile, Long> {
    Optional<ReaderProfile> findByLibraryCardNumber(String libraryCardNumber);
    Optional<ReaderProfile> findByUserId(Long userId);
    boolean existsByLibraryCardNumber(String libraryCardNumber);
}


