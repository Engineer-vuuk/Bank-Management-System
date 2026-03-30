package com.bms.bank.management.system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private String token;
    private String username;  // Added username field
    private String role;
    private String accountNumber;
}
