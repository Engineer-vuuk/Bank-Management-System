package com.bms.bank.management.system.dto;

public class WithdrawRequest {
    private String accountNumber;
    private double amount;
    private String agentNumber;
    private String pin;

    // ✅ Constructors
    public WithdrawRequest() {}

    public WithdrawRequest(String accountNumber, double amount, String agentNumber, String pin) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.agentNumber = agentNumber;
        this.pin = pin;
    }

    // ✅ Getters and Setters
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

    public String getAgentNumber() {
        return agentNumber;
    }

    public void setAgentNumber(String agentNumber) {
        this.agentNumber = agentNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
