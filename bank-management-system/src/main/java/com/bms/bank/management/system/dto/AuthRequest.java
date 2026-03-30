package com.bms.bank.management.system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String username;
    private String email;
    private String fullName;
    private String dob;
    private String kinName;
    private String phoneNumber;
    private String kinPhoneNumber;
    private String idNumber;
    private String pin; // ✅ Used instead of password
    private String role; // ✅ Default role: USER

    public AuthRequest(String username, String email, String fullName, String dob,
                       String kinName, String phoneNumber, String kinPhoneNumber,
                       String idNumber, String pin, String role) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.dob = dob;
        this.kinName = kinName;
        this.phoneNumber = phoneNumber;
        this.kinPhoneNumber = kinPhoneNumber;
        this.idNumber = idNumber;
        this.pin = pin;
        this.role = role;
    }
}
