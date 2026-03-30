package com.bms.bank.management.system.service;

import com.bms.bank.management.system.dto.AdminDashboardResponse;
import com.bms.bank.management.system.model.User;
import com.bms.bank.management.system.model.Admin;
import com.bms.bank.management.system.repository.AdminRepository;
import com.bms.bank.management.system.repository.TransactionRepository;
import com.bms.bank.management.system.repository.UserRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final JavaMailSender mailSender;
    private final AdminRepository adminRepository;

    public AdminService(UserRepository userRepository,
                        TransactionRepository transactionRepository,
                        JavaMailSender mailSender,
                        AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.mailSender = mailSender;
        this.adminRepository = adminRepository;
    }

    Admin getLoggedInAdmin() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        return adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found for username: " + username));
    }

    private void sendStatusEmail(String toEmail, String username, String status) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Account Status Update");
            String text = String.format("Hello %s,<br><br>Your account has been <strong>%s</strong>.<br><br>Thank you.<br>Bank Management System",
                    username, status.toLowerCase());
            helper.setText(text, true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AdminDashboardResponse getDashboardData() {
        int totalUsers = (int) userRepository.count();

        List<String> recentTransactions = transactionRepository.findTop10ByOrderByTimestampDesc()
                .stream()
                .map(transaction -> String.format("Transaction ID: %d, Amount: %.2f, Date: %s",
                        transaction.getId(), transaction.getAmount(), transaction.getTimestamp()))
                .collect(Collectors.toList());

        return new AdminDashboardResponse(0, totalUsers, recentTransactions, List.of());
    }
}
