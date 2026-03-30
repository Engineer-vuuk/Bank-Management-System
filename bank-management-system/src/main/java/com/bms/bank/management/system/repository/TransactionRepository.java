package com.bms.bank.management.system.repository;

import com.bms.bank.management.system.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // 🔹 Fetch transactions by account number
    List<Transaction> findByAccountNumber(String accountNumber);

    // 🔹 Fetch transactions by transaction type (e.g., DEPOSIT, WITHDRAWAL)
    List<Transaction> findByTransactionType(String transactionType);

    // 🔹 Fetch transactions by user ID
    List<Transaction> findByUserId(Long userId);

    // 🔹 Fetch transactions within a specific date range
    List<Transaction> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 🔹 Fetch transactions by account number and transaction type
    List<Transaction> findByAccountNumberAndTransactionType(String accountNumber, String transactionType);

    // 🔹 Fetch all transactions ordered by timestamp (for admin's full view)
    List<Transaction> findAllByOrderByTimestampDesc();

    // ✅ Fetch the latest 10 transactions (admin recent activity view)
    List<Transaction> findTop10ByOrderByTimestampDesc();
}