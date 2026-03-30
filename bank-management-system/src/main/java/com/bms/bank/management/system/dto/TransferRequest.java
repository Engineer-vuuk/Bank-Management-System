package com.bms.bank.management.system.dto;

public class TransferRequest {

    private String fromAccount;
    private String toAccount;
    private double amount;
    private String pin;

    // Getters and Setters
    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    // Constructor
    public TransferRequest(String fromAccount, String toAccount, double amount, String pin) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.pin = pin;
    }

    // Default constructor
    public TransferRequest() {
    }
}