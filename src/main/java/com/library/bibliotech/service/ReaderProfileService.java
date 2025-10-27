package com.library.bibliotech.service;

import com.library.bibliotech.model.ReaderProfile;
import com.library.bibliotech.repository.ReaderProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReaderProfileService {
    
    @Autowired
    private ReaderProfileRepository readerProfileRepository;
    
    public List<ReaderProfile> getAllProfiles() {
        return readerProfileRepository.findAll();
    }
    
    public Page<ReaderProfile> getAllProfiles(Pageable pageable) {
        return readerProfileRepository.findAll(pageable);
    }
    
    public Optional<ReaderProfile> getProfileById(Long id) {
        return readerProfileRepository.findById(id);
    }
    
    public Optional<ReaderProfile> getProfileByUserId(Long userId) {
        return readerProfileRepository.findByUserId(userId);
    }
    
    public ReaderProfile updateProfile(Long id, ReaderProfile profileDetails) {
        Optional<ReaderProfile> profileOpt = readerProfileRepository.findById(id);
        if (profileOpt.isPresent()) {
            ReaderProfile profile = profileOpt.get();
            profile.setFirstName(profileDetails.getFirstName());
            profile.setLastName(profileDetails.getLastName());
            profile.setPhoneNumber(profileDetails.getPhoneNumber());
            return readerProfileRepository.save(profile);
        }
        return null;
    }
    
    public void updateLoanLimit(Long id, int limit) {
        Optional<ReaderProfile> profileOpt = readerProfileRepository.findById(id);
        if (profileOpt.isPresent()) {
            ReaderProfile profile = profileOpt.get();
            profile.setLoanLimit(limit);
            readerProfileRepository.save(profile);
        }
    }
}


