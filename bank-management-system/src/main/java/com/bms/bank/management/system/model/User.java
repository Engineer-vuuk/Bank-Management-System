package com.bms.bank.management.system.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String dob;

    @Column(nullable = false)
    private String kinName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String kinPhoneNumber;

    @Column(nullable = false, unique = true)
    private String idNumber;

    @Column(nullable = false, length = 60)
    private String pin;

    @Column(nullable = false, length = 60)
    private String transactionPin;

    @Column(nullable = false, unique = true, length = 13)
    private String accountNumber;

    @Column(nullable = false)
    private double balance = 0.0;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Transaction> transactions;

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void setPin(String rawPin) {
        if (!rawPin.startsWith("$2a$")) {
            String hashedPin = passwordEncoder.encode(rawPin);
            this.pin = hashedPin;
            this.transactionPin = hashedPin;
        } else {
            this.pin = rawPin;
            this.transactionPin = rawPin;
        }
    }

    public boolean isPinValid(String rawPin) {
        return passwordEncoder.matches(rawPin, this.pin);
    }

    public boolean isTransactionPinValid(String rawPin) {
        return passwordEncoder.matches(rawPin, this.transactionPin);
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
}
