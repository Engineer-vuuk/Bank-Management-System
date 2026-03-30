package com.bms.bank.management.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @OneToOne(cascade = CascadeType.PERSIST)  // Cascade to persist User when creating Account
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String dob;

    @Column(nullable = false)
    private String nextOfKin;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String kinPhoneNumber;

    @Column(nullable = false, unique = true)
    private String idNumber;

    @JsonIgnore  // Prevents transaction PIN from being exposed in API responses
    @Column(nullable = false)
    private String transactionPin;

    @Column(nullable = false, precision = 18, scale = 2) // Ensures proper storage of money values
    private BigDecimal balance = BigDecimal.ZERO;

    // Constructors
    public Account() {}

    public Account(String accountNumber, User user, String fullName, String email, String dob,
                   String nextOfKin, String phoneNumber, String kinPhoneNumber, String idNumber,
                   String transactionPin, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.user = user;
        this.fullName = fullName;
        this.email = email;
        this.dob = dob;
        this.nextOfKin = nextOfKin;
        this.phoneNumber = phoneNumber;
        this.kinPhoneNumber = kinPhoneNumber;
        this.idNumber = idNumber;
        this.transactionPin = transactionPin;
        this.balance = balance;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(String nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getKinPhoneNumber() {
        return kinPhoneNumber;
    }

    public void setKinPhoneNumber(String kinPhoneNumber) {
        this.kinPhoneNumber = kinPhoneNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getTransactionPin() {
        return transactionPin;
    }

    public void setTransactionPin(String transactionPin) {
        this.transactionPin = transactionPin;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
