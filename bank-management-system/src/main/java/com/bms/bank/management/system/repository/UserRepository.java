package com.bms.bank.management.system.repository;

import com.bms.bank.management.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);            // Fetch user by email

    boolean existsByEmail(String email);                 // Check if email already exists

    Optional<User> findByAccountNumber(String accountNumber); // Fetch user by account number

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByUsername(String username);           // Check if username already exists

    Optional<User> findByUsername(String username);      // Fetch user by username

}
