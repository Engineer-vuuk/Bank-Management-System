package com.bms.bank.management.system.dto;

public class DepositRequest {
    private String agentNumber;
    private String accountNumber;
    private double amount;
    private String pin; // ✅ Renamed for consistency with TransactionService

    public DepositRequest() {
    }

    public DepositRequest(String agentNumber, String accountNumber, double amount, String pin) {
        this.agentNumber = agentNumber;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.pin = pin;
    }

    public String getAgentNumber() {
        return agentNumber;
    }

    public void setAgentNumber(String agentNumber) {
        this.agentNumber = agentNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPin() { // ✅ Updated getter name
        return pin;
    }

    public void setPin(String pin) { // ✅ Updated setter name
        this.pin = pin;
    }
}
