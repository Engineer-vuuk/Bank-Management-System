package com.bms.bank.management.system.repository;

import com.bms.bank.management.system.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByAgentNumber(String agentNumber);
}
