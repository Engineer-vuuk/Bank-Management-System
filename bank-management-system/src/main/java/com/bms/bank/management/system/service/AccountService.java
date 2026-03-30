package com.bms.bank.management.system.service;

import com.bms.bank.management.system.model.Account;
import com.bms.bank.management.system.model.User;
import com.bms.bank.management.system.repository.AccountRepository;
import com.bms.bank.management.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public String openAccount(Account accountData, String username) {
        // Check if the user exists
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return "User not found";
        }

        User user = userOptional.get();

        // Check if the user already has an account
        if (accountRepository.existsByAccountNumber(accountData.getAccountNumber())) {
            return "Account already exists with that number";
        }

        // Create a new account for the user
        Account account = new Account();
        account.setAccountNumber(UUID.randomUUID().toString());
        account.setUser(user);
        account.setFullName(accountData.getFullName());
        account.setEmail(accountData.getEmail());
        account.setDob(accountData.getDob());
        account.setNextOfKin(accountData.getNextOfKin());
        account.setPhoneNumber(accountData.getPhoneNumber());
        account.setKinPhoneNumber(accountData.getKinPhoneNumber());
        account.setIdNumber(accountData.getIdNumber());
        account.setTransactionPin(accountData.getTransactionPin());
        account.setBalance(BigDecimal.ZERO);

        // Save the account
        accountRepository.save(account);

        return "Account opened successfully";
    }

    // Fetch account by account number (no longer using username)
    public Optional<Account> getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }
}