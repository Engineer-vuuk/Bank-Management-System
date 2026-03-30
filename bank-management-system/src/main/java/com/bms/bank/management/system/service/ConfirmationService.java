package com.bms.bank.management.system.service;

import com.bms.bank.management.system.model.ConfirmationToken;
import com.bms.bank.management.system.model.User;
import com.bms.bank.management.system.model.Account;
import com.bms.bank.management.system.repository.ConfirmationTokenRepository;
import com.bms.bank.management.system.repository.UserRepository;
import com.bms.bank.management.system.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmationService.class);

    @Autowired
    private ConfirmationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;  // Repository to save Account

    @Autowired
    private JavaMailSender mailSender;

    // Create and send confirmation token via email
    public void sendConfirmationEmail(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(24),  // Token expires in 24 hours
                user
        );
        tokenRepository.save(confirmationToken);

        // Send confirmation email
        String confirmationLink = "http://localhost:5173/confirm?token=" + token;
        sendEmail(user.getEmail(), confirmationLink);
    }

    // Helper method to send the email
    private void sendEmail(String email, String confirmationLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Confirm Your Account");
            message.setText("Click the following link to confirm your account: " + confirmationLink);
            mailSender.send(message);
            LOGGER.info("Confirmation email sent to {}", email);
        } catch (Exception e) {
            LOGGER.error("Error sending confirmation email to {}: {}", email, e.getMessage());
            throw new RuntimeException("Error sending confirmation email.");
        }
    }

    public boolean confirmAccount(String token) {
        ConfirmationToken confirmationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Invalid or expired token"));

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            LOGGER.warn("Token has expired: {}", token);
            throw new IllegalStateException("Token has expired");
        }

        User user = confirmationToken.getUser();
        userRepository.save(user); // Still saving the user if needed (optional now)

        // Create an Account for the user after confirmation
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

        accountRepository.save(account);

        return true;
    }
}
