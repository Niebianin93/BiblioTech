package com.library.bibliotech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
    
    public void sendRegistrationConfirmation(String email, String username, String activationToken) {
        String subject = "Activate Your BiblioTech Account";
        String activationLink = "http://localhost:8080/activate?token=" + activationToken;
        String text = "Hello " + username + ",\n\n" +
                     "Thank you for registering at BiblioTech Library!\n\n" +
                     "Please click the link below to activate your account:\n" +
                     activationLink + "\n\n" +
                     "If you did not register, please ignore this email.\n\n" +
                     "Best regards,\nBiblioTech Team";
        sendEmail(email, subject, text);
    }
    
    public void sendLoanReminder(String email, String bookTitle, String dueDate) {
        String subject = "Loan Due Date Reminder";
        String text = "Hello,\n\n" +
                     "This is a reminder that your loan of '" + bookTitle + "' is due on " + dueDate + ".\n" +
                     "Please return the book on time to avoid penalties.\n\n" +
                     "Best regards,\nBiblioTech Team";
        sendEmail(email, subject, text);
    }
    
    public void sendOverdueNotification(String email, String bookTitle) {
        String subject = "Overdue Loan Notice";
        String text = "Hello,\n\n" +
                     "Your loan of '" + bookTitle + "' is overdue.\n" +
                     "Please return the book as soon as possible.\n\n" +
                     "Best regards,\nBiblioTech Team";
        sendEmail(email, subject, text);
    }
    
    public void sendReservationAvailable(String email, String bookTitle) {
        String subject = "Reserved Book Available";
        String text = "Hello,\n\n" +
                     "The book '" + bookTitle + "' you reserved is now available.\n" +
                     "Please collect it within 3 days.\n\n" +
                     "Best regards,\nBiblioTech Team";
        sendEmail(email, subject, text);
    }
}


