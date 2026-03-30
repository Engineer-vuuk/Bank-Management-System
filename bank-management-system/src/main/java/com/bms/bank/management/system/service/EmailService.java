package com.bms.bank.management.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    // ✅ Existing method for transfer confirmation
    public void sendTransferConfirmationEmail(String toEmail, String recipientName, String transferAmount, String dateTime, String transactionFee) {
        String subject = "Transfer Confirmation";
        String body = String.format(
                "Dear %s,\n\n" +
                        "We would like to inform you that a transfer of ₦%s was successfully completed on %s.\n" +
                        "Transaction Fee: ₦%s\n\n" +
                        "Thank you for banking with us.\n\n" +
                        "Sincerely,\nBank Management System",
                recipientName, transferAmount, dateTime, transactionFee
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }

    // ✅ Updated welcome email method without account number
    public void sendWelcomeEmail(String toEmail, String fullName) {
        String subject = "Welcome to SmartBank!";
        String body = String.format(
                "Hello %s,\n\n" +
                        "Welcome to SmartBank! 🎉 Your account has been successfully created.\n\n" +
                        "We're thrilled to have you onboard. Your journey to smarter banking starts now.\n\n" +
                        "If you ever need assistance, our support team is just an email away.\n\n" +
                        "Warm regards,\nSmartBank Team",
                fullName
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }
}
