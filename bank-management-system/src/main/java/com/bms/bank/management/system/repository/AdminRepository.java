package com.bms.bank.management.system.repository;

import com.bms.bank.management.system.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Custom query method to find an admin by their username
    Optional<Admin> findByUsername(String username);
}
