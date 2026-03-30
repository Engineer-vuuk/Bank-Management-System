package com.bms.bank.management.system.controller;

import com.bms.bank.management.system.model.Account;
import com.bms.bank.management.system.model.Transaction;
import com.bms.bank.management.system.model.User;
import com.bms.bank.management.system.service.AccountService;
import com.bms.bank.management.system.service.UserService;
import com.bms.bank.management.system.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private static final Logger logger = Logger.getLogger(AccountController.class.getName());

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    // Endpoint to get account details using username from JWT
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/details")
    public ResponseEntity<?> getAccountDetails(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername(); // Extract username from JWT
        logger.info("Fetching account details for user: " + username);

        // Retrieve user by username
        Optional<User> userOptional = userService.getUserByUsername(username);
        if (userOptional.isEmpty()) {
            logger.warning("User not found: " + username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = userOptional.get();
        if (user.getAccountNumber() == null) {
            logger.warning("No account found for user: " + username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        }

        // Retrieve account using the fetched account number
        Optional<Account> account = accountService.getAccountByAccountNumber(user.getAccountNumber());
        if (account.isEmpty()) {
            logger.warning("No account found for account number: " + user.getAccountNumber());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        }

        logger.info("Account details retrieved successfully for user: " + username);
        return ResponseEntity.ok(account.get());
    }

    // Endpoint to get transactions for the logged-in user
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/transactions")
    public ResponseEntity<?> getUserTransactions(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername(); // Extract username from JWT
        logger.info("Fetching transactions for user: " + username);

        // Retrieve user by username
        Optional<User> userOptional = userService.getUserByUsername(username);
        if (userOptional.isEmpty()) {
            logger.warning("User not found: " + username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = userOptional.get();
        if (user.getAccountNumber() == null) {
            logger.warning("No account found for user: " + username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        }

        // Fetch transactions for the user's account
        List<Transaction> transactions = transactionRepository.findByAccountNumber(user.getAccountNumber());
        if (transactions.isEmpty()) {
            logger.warning("No transactions found for account number: " + user.getAccountNumber());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No transactions found.");
        }

        logger.info("Transactions retrieved successfully for user: " + username);
        return ResponseEntity.ok(transactions);
    }
}
