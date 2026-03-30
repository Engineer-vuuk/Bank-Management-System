package com.bms.bank.management.system.config;

import com.bms.bank.management.system.model.Agent;
import com.bms.bank.management.system.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class AgentSeeder {

    @Autowired
    private AgentRepository agentRepository;

    @PostConstruct
    public void seedAgent() {
        if (agentRepository.count() == 0) {
            Agent agent = new Agent();
            agent.setAgentNumber("AGENT12345"); // Default agent number
            agent.setFloatBalance(10000000.00); // 10 Million Float
            agentRepository.save(agent);
            System.out.println("Agent with 10M float balance initialized.");
        }
    }
}
