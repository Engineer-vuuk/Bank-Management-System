package com.bms.bank.management.system.service;

import com.bms.bank.management.system.model.User;
import com.bms.bank.management.system.model.Account;
import com.bms.bank.management.system.repository.UserRepository;
import com.bms.bank.management.system.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailService emailService; // Inject EmailService

    // Method to fetch user by username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Register user and create an account for the user automatically within a transaction
    @Transactional
    public User registerUser(User user) {
        try {
            // Save user in the repository
            userRepository.save(user);
            LOGGER.info("User registered: {}", user.getUsername());

            // Automatically create an account for the user
            Account account = createAccountForUser(user);

            // Send welcome email without account number
            emailService.sendWelcomeEmail(user.getEmail(), user.getFullName());

            return user;
        } catch (Exception e) {
            LOGGER.error("Error registering user: {}", e.getMessage());
            throw new RuntimeException("Error during user registration and account creation", e);
        }
    }

    // Method to create an account for the user
    private Account createAccountForUser(User user) {
        LOGGER.info("Creating account for user: {}", user.getUsername());

        Account account = new Account();
        account.setUser(user);
        account.setFullName(user.getFullName());
        account.setEmail(user.getEmail());
        account.setDob(user.getDob());
        account.setNextOfKin(user.getKinName());
        account.setPhoneNumber(user.getPhoneNumber());
        account.setKinPhoneNumber(user.getKinPhoneNumber());
        account.setIdNumber(user.getIdNumber());
        account.setTransactionPin(user.getTransactionPin());
        account.setBalance(BigDecimal.ZERO);

        String accountNumber = generateAccountNumber();
        account.setAccountNumber(accountNumber);

        accountRepository.save(account);
        LOGGER.info("Account created for user: {} with account number: {}", user.getUsername(), accountNumber);

        return account;
    }

    // Generate a unique account number
    private String generateAccountNumber() {
        return "AC" + System.currentTimeMillis();
    }
}
