package com.library.bibliotech.service;

import com.library.bibliotech.model.ReaderProfile;
import com.library.bibliotech.model.User;
import com.library.bibliotech.repository.ReaderProfileRepository;
import com.library.bibliotech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ReaderProfileRepository readerProfileRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    @Transactional
    public User registerUser(String username, String email, String password, String firstName, String lastName, String phone) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        try {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setEnabled(false);
            
            String activationToken = UUID.randomUUID().toString();
            user.setActivationToken(activationToken);
            
            Set<String> roles = new HashSet<>();
            roles.add("ROLE_READER");
            user.setRoles(roles);
            
            user = userRepository.save(user);
            
            ReaderProfile profile = new ReaderProfile();
            profile.setUser(user);
            profile.setFirstName(firstName);
            profile.setLastName(lastName);
            profile.setPhoneNumber(phone);
            profile.setLibraryCardNumber(generateCardNumber());
            profile.setLoanLimit(5);
            readerProfileRepository.save(profile);
            
            emailService.sendRegistrationConfirmation(email, username, activationToken);
            
            return user;
        } catch (DataIntegrityViolationException e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("username")) {
                throw new RuntimeException("Username already exists");
            } else if (msg != null && msg.contains("email")) {
                throw new RuntimeException("Email already exists");
            } else {
                throw new RuntimeException("Registration failed - duplicate entry");
            }
        }
    }
    
    private String generateCardNumber() {
        String cardNumber;
        do {
            cardNumber = "LIB" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (readerProfileRepository.existsByLibraryCardNumber(cardNumber));
        return cardNumber;
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public void enableUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEnabled(true);
            userRepository.save(user);
        }
    }
    
    public void addRole(Long userId, String role) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }
    
    public void setRole(Long userId, String role) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Set<String> newRoles = new HashSet<>();
            newRoles.add(role);
            user.setRoles(newRoles);
            userRepository.save(user);
        }
    }
    
    public void removeRole(Long userId, String role) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.getRoles().remove(role);
            userRepository.save(user);
        }
    }
    
    public void updateUser(User user) {
        userRepository.save(user);
    }
    
    public boolean activateUser(String token) {
        Optional<User> userOpt = userRepository.findByActivationToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEnabled(true);
            user.setActivationToken(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}


