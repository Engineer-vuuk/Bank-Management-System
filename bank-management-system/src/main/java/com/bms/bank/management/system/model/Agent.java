package com.bms.bank.management.system.model;

import jakarta.persistence.*;

@Entity
@Table(name = "agents")
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String agentNumber;

    @Column(nullable = false)
    private double floatBalance;

    public Agent() {
    }

    public Agent(String agentNumber, double floatBalance) {
        this.agentNumber = agentNumber;
        this.floatBalance = floatBalance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentNumber() {
        return agentNumber;
    }

    public void setAgentNumber(String agentNumber) {
        this.agentNumber = agentNumber;
    }

    public double getFloatBalance() {
        return floatBalance;
    }

    public void setFloatBalance(double floatBalance) {
        this.floatBalance = floatBalance;
    }
}
