package com.bms.bank.management.system.repository;

import com.bms.bank.management.system.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.user.username = :username")
    Optional<Account> findByUsername(@Param("username") String username);

    @Query("SELECT a FROM Account a WHERE a.balance BETWEEN :minBalance AND :maxBalance")
    List<Account> findAccountsByBalanceRange(@Param("minBalance") BigDecimal minBalance, @Param("maxBalance") BigDecimal maxBalance);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Account a WHERE a.user.username = :username")
    boolean existsByUsername(@Param("username") String username);

    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
}

