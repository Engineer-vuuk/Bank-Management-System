package com.bms.bank.management.system.repository;

import com.bms.bank.management.system.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);  // Find token by its unique value
}
