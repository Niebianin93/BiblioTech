package com.library.bibliotech.config;

import com.library.bibliotech.model.*;
import com.library.bibliotech.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ReaderProfileRepository readerProfileRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@bibliotech.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);
            Set<String> adminRoles = new HashSet<>();
            adminRoles.add("ROLE_ADMIN");
            admin.setRoles(adminRoles);
            userRepository.save(admin);

            ReaderProfile adminProfile = new ReaderProfile();
            adminProfile.setUser(admin);
            adminProfile.setFirstName("Admin");
            adminProfile.setLastName("User");
            adminProfile.setPhoneNumber("111111111");
            adminProfile.setLibraryCardNumber("LIB00000");
            adminProfile.setLoanLimit(0);
            readerProfileRepository.save(adminProfile);

            User librarian = new User();
            librarian.setUsername("librarian");
            librarian.setEmail("librarian@bibliotech.com");
            librarian.setPassword(passwordEncoder.encode("lib123"));
            librarian.setEnabled(true);
            Set<String> libRoles = new HashSet<>();
            libRoles.add("ROLE_LIBRARIAN");
            librarian.setRoles(libRoles);
            userRepository.save(librarian);

            ReaderProfile libProfile = new ReaderProfile();
            libProfile.setUser(librarian);
            libProfile.setFirstName("Sarah");
            libProfile.setLastName("Johnson");
            libProfile.setPhoneNumber("222222222");
            libProfile.setLibraryCardNumber("LIB00099");
            libProfile.setLoanLimit(0);
            readerProfileRepository.save(libProfile);

            String[] firstNames = {"John", "Jane", "Bob", "Alice", "Mike", "Emma", "Tom", "Lisa", 
                "David", "Sarah", "Chris", "Anna", "Mark", "Laura", "Paul", "Jessica", "Kevin", 
                "Michelle", "Brian", "Amanda", "Steven", "Nicole", "Daniel", "Melissa", "Matthew", 
                "Stephanie", "Andrew", "Jennifer", "Joshua", "Rachel", "Eric", "Rebecca"};
            
            String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Davis", "Miller", 
                "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", 
                "Martin", "Thompson", "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", 
                "Lewis", "Lee", "Walker", "Hall", "Allen", "Young", "King", "Wright", "Lopez"};

            for (int i = 0; i < 30; i++) {
                User reader = new User();
                reader.setUsername("reader" + (i + 1));
                reader.setEmail("reader" + (i + 1) + "@example.com");
                reader.setPassword(passwordEncoder.encode("pass123"));
                reader.setEnabled(true);
                Set<String> readerRoles = new HashSet<>();
                readerRoles.add("ROLE_READER");
                reader.setRoles(readerRoles);
                userRepository.save(reader);

                ReaderProfile profile = new ReaderProfile();
                profile.setUser(reader);
                profile.setFirstName(firstNames[i % firstNames.length]);
                profile.setLastName(lastNames[i % lastNames.length]);
                profile.setPhoneNumber("55512" + String.format("%04d", i + 1));
                profile.setLibraryCardNumber("LIB" + String.format("%05d", i + 1));
                profile.setLoanLimit(3 + (i % 3));
                readerProfileRepository.save(profile);
            }

            String[] bookTitles = {
                "Java Programming", "Clean Code", "Design Patterns", "Spring in Action",
                "Effective Java", "Head First Java", "Refactoring", "Introduction to Algorithms",
                "The Pragmatic Programmer", "Code Complete", "Thinking in Java", "JavaScript: The Good Parts",
                "Python Crash Course", "Database System Concepts", "Operating System Concepts",
                "Computer Networks", "Artificial Intelligence", "Machine Learning", "Deep Learning",
                "Data Structures and Algorithms", "Software Engineering", "Web Development",
                "Mobile App Development", "Cloud Computing", "Cybersecurity Fundamentals",
                "Blockchain Technology", "Internet of Things", "Big Data Analytics", "DevOps Handbook",
                "Microservices Architecture", "RESTful API Design", "Node.js Development",
                "React Programming", "Angular Development", "Vue.js Guide", "TypeScript Handbook"
            };

            String[] authors = {
                "James Gosling", "Robert Martin", "Gang of Four", "Craig Walls", "Joshua Bloch",
                "Kathy Sierra", "Martin Fowler", "Thomas Cormen", "Andrew Hunt", "Steve McConnell",
                "Bruce Eckel", "Douglas Crockford", "Eric Matthes", "Abraham Silberschatz",
                "William Stallings", "Andrew Tanenbaum", "Stuart Russell", "Tom Mitchell",
                "Ian Goodfellow", "Michael Goodrich", "Roger Pressman", "Jon Duckett",
                "Paul Deitel", "Thomas Erl", "William Easttom", "Andreas Antonopoulos",
                "Adrian McEwen", "Nathan Marz", "Gene Kim", "Sam Newman", "Leonard Richardson"
            };

            String[] publishers = {
                "Oracle Press", "Prentice Hall", "Addison-Wesley", "Manning", "O'Reilly",
                "Wiley", "McGraw-Hill", "MIT Press", "Microsoft Press", "Apress", "Packt",
                "No Starch Press", "Pragmatic Bookshelf", "Springer", "Cambridge University Press"
            };

            for (int i = 0; i < bookTitles.length; i++) {
                Book book = new Book();
                book.setTitle(bookTitles[i]);
                book.setAuthor(authors[i % authors.length]);
                book.setIsbn("978-" + String.format("%d-%02d-%06d-%d", 
                    (i % 3), (i % 99), (100000 + i * 123), (i % 10)));
                book.setPublisher(publishers[i % publishers.length]);
                book.setPublicationYear(2000 + (i % 24));
                int totalCopies = 1 + (i % 4);
                book.setTotalCopies(totalCopies);
                book.setAvailableCopies(totalCopies);
                bookRepository.save(book);
            }

            ReaderProfile[] profiles = readerProfileRepository.findAll().toArray(new ReaderProfile[0]);
            Book[] books = bookRepository.findAll().toArray(new Book[0]);

            for (int i = 0; i < 35; i++) {
                if (i >= profiles.length || i >= books.length) break;
                
                ReaderProfile profile = profiles[i % profiles.length];
                Book book = books[i % books.length];
                
                if (book.getAvailableCopies() > 0) {
                    Loan loan = new Loan();
                    loan.setReaderProfile(profile);
                    loan.setBook(book);
                    
                    int daysOffset = i % 20;
                    loan.setLoanDate(LocalDate.now().minusDays(daysOffset));
                    loan.setDueDate(LocalDate.now().plusDays(14 - daysOffset));
                    
                    if (i % 7 == 0) {
                        loan.setStatus("RETURNED");
                        loan.setReturnDate(LocalDate.now().minusDays(1));
                    } else {
                        loan.setStatus("ACTIVE");
                        book.setAvailableCopies(book.getAvailableCopies() - 1);
                        bookRepository.save(book);
                    }
                    
                    loanRepository.save(loan);
                }
            }

            for (int i = 0; i < 32; i++) {
                if (i >= profiles.length || i >= books.length) break;
                
                ReaderProfile profile = profiles[i % profiles.length];
                Book book = books[(i + 10) % books.length];
                
                Reservation reservation = new Reservation();
                reservation.setReaderProfile(profile);
                reservation.setBook(book);
                reservation.setReservationDate(LocalDate.now().minusDays(i % 15));
                reservation.setStatus(i % 8 == 0 ? "COMPLETED" : "PENDING");
                reservationRepository.save(reservation);
            }

        }
    }
}


