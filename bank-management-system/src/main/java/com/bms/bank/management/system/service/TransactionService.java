package com.bms.bank.management.system.service;

import com.bms.bank.management.system.dto.DepositRequest;
import com.bms.bank.management.system.dto.WithdrawRequest;
import com.bms.bank.management.system.dto.TransferRequest;
import com.bms.bank.management.system.model.User;
import com.bms.bank.management.system.model.Account;
import com.bms.bank.management.system.model.Transaction;
import com.bms.bank.management.system.repository.UserRepository;
import com.bms.bank.management.system.repository.AccountRepository;
import com.bms.bank.management.system.repository.TransactionRepository;
import com.bms.bank.management.system.exception.AccountNotFoundException;
import com.bms.bank.management.system.exception.InvalidPinException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Deposit method
    @Transactional
    public Map<String, Object> deposit(DepositRequest request) {
        Map<String, Object> response = new HashMap<>();

        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        User user = userRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPin(), user.getTransactionPin())) {
            throw new InvalidPinException("Invalid PIN");
        }

        BigDecimal depositAmount = BigDecimal.valueOf(request.getAmount());
        account.setBalance(account.getBalance().add(depositAmount));
        accountRepository.save(account);

        // ✅ Use agent number from request
        String agentNumber = request.getAgentNumber();
        LocalDateTime now = LocalDateTime.now();

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAccount(account);
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(depositAmount.doubleValue());
        transaction.setAccountNumber(request.getAccountNumber());
        transaction.setAgentNumber(agentNumber);
        transaction.setTimestamp(now);

        transactionRepository.save(transaction);

        response.put("message", "Deposit successful");
        response.put("newBalance", account.getBalance());

        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        emailService.sendTransferConfirmationEmail(
                user.getEmail(), user.getFullName(),
                String.format("%.2f", depositAmount),
                formattedDateTime,
                "0.00"
        );

        return response;
    }

    // Withdraw method
    @Transactional
    public Map<String, Object> withdraw(WithdrawRequest request) {
        Map<String, Object> response = new HashMap<>();

        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        User user = userRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPin(), user.getTransactionPin())) {
            throw new InvalidPinException("Invalid PIN");
        }

        BigDecimal withdrawAmount = BigDecimal.valueOf(request.getAmount());
        if (account.getBalance().compareTo(withdrawAmount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(withdrawAmount));
        accountRepository.save(account);

        // ✅ Use agent number from request
        String agentNumber = request.getAgentNumber();
        LocalDateTime now = LocalDateTime.now();

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAccount(account);
        transaction.setTransactionType("WITHDRAWAL");
        transaction.setAmount(withdrawAmount.doubleValue());
        transaction.setAccountNumber(request.getAccountNumber());
        transaction.setAgentNumber(agentNumber);
        transaction.setTimestamp(now);

        transactionRepository.save(transaction);

        response.put("message", "Withdrawal successful");
        response.put("newBalance", account.getBalance());

        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        emailService.sendTransferConfirmationEmail(
                user.getEmail(), user.getFullName(),
                String.format("%.2f", withdrawAmount),
                formattedDateTime,
                "0.00"
        );

        return response;
    }

    // Transfer method
    @Transactional
    public Map<String, Object> transfer(TransferRequest request) {
        Map<String, Object> response = new HashMap<>();

        Account senderAccount = accountRepository.findByAccountNumber(request.getFromAccount())
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));

        Account recipientAccount = accountRepository.findByAccountNumber(request.getToAccount())
                .orElseThrow(() -> new AccountNotFoundException("Recipient account not found"));

        User senderUser = userRepository.findByAccountNumber(request.getFromAccount())
                .orElseThrow(() -> new RuntimeException("Sender user not found"));

        if (!passwordEncoder.matches(request.getPin(), senderUser.getTransactionPin())) {
            throw new InvalidPinException("Invalid PIN for sender");
        }

        User recipientUser = userRepository.findByAccountNumber(request.getToAccount())
                .orElseThrow(() -> new RuntimeException("Recipient user not found"));

        BigDecimal senderBalance = senderAccount.getBalance();
        BigDecimal transferAmount = BigDecimal.valueOf(request.getAmount());

        if (senderBalance.compareTo(transferAmount) < 0) {
            throw new RuntimeException("Insufficient balance for transfer");
        }

        BigDecimal transactionFee = transferAmount.multiply(BigDecimal.valueOf(0.0025));
        BigDecimal totalDeducted = transferAmount.add(transactionFee);

        if (senderBalance.compareTo(totalDeducted) < 0) {
            throw new RuntimeException("Insufficient funds after deducting transaction fee");
        }

        senderAccount.setBalance(senderBalance.subtract(totalDeducted));
        recipientAccount.setBalance(recipientAccount.getBalance().add(transferAmount));

        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);

        String agentNumber = "SELF";
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Transaction senderTransaction = new Transaction();
        senderTransaction.setUser(senderUser);
        senderTransaction.setAccount(senderAccount);
        senderTransaction.setTransactionType("SENT");
        senderTransaction.setAmount(transferAmount.doubleValue());
        senderTransaction.setAccountNumber(request.getFromAccount());
        senderTransaction.setAgentNumber(request.getToAccount()); // ✅ shows receiver account
        senderTransaction.setTimestamp(now);

        transactionRepository.save(senderTransaction);

        Transaction recipientTransaction = new Transaction();
        recipientTransaction.setUser(recipientUser);
        recipientTransaction.setAccount(recipientAccount);
        recipientTransaction.setTransactionType("RECEIVED");
        recipientTransaction.setAmount(transferAmount.doubleValue());
        recipientTransaction.setAccountNumber(request.getToAccount());
        recipientTransaction.setAgentNumber(request.getFromAccount()); // ✅ shows sender account
        recipientTransaction.setTimestamp(now);

        transactionRepository.save(recipientTransaction);

        emailService.sendTransferConfirmationEmail(
                senderUser.getEmail(), senderUser.getFullName(),
                String.format("%.2f", transferAmount),
                formattedDateTime,
                String.format("%.2f", transactionFee)
        );

        emailService.sendTransferConfirmationEmail(
                recipientUser.getEmail(), recipientUser.getFullName(),
                String.format("%.2f", transferAmount),
                formattedDateTime,
                "0.00"
        );

        String successMessage = String.format(
                "%.2f successfully sent to %s at %s. Keep growing your account! Transaction fee: %.2f",
                transferAmount.doubleValue(),
                recipientUser.getFullName(),
                formattedDateTime,
                transactionFee.doubleValue()
        );

        response.put("message", successMessage);
        response.put("newBalance", senderAccount.getBalance());

        return response;
    }
}